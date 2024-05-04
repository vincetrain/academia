def semesterCourseIsValid(course_id):
    '''
    Checks if provided course_id is valid

    ...
    course_id must be alphanumeric and have a length of 6 characters, starting with 3 letters, and ending with 3 integers.

    Parameters
    ----------
    course_id : str
        A string containing the course id

    Returns
    -------
    True : bool
        if course_id is valid
    False : bool
        if course_id is invalid
    '''
    if len(course_id) != 6:
        return False
    for char in course_id[:3]:
        if not char.isalpha():
            return False
    for char in course_id[3:]:
        if not char.isdigit():
            return False
    return True

def studentNameIsValid(student_name):
    '''
    Checks if provided student_name is correct

    ...
    student_name must be alphabetical.

    Parameters
    ----------
    student_name : str
        A string containing the student's name

    Returns
    -------
    True : bool
        if student_name is valid
    False : bool
        if student_name is invalid
    '''
    for char in student_name:
        if not (char.isalpha() or char == " "):
            return False
    return True

def sanitizeStudentName(student_name):
    '''
    Returns a sanitized version of student_name

    ... Capitalizes first and last name's first characters. Removes white spaces that may be trailing or leading.

    Parameters
    ----------
    student_name : str
        String containing the student's name to be sanitized
    
    Returns
    -------
    sanitized_name : str
        Sanitized version of the student's name
    '''
    
    sanitized_name = ""
    student_name_split = student_name.split(" ") # splits name to isolate first and lastnames and other names
    # iterates through the name splits
    for i in student_name_split:
        # initializes namePart, which is the part of the name. makes sure to .strip to remove useless whitespaces
        namePart = student_name_split[i].strip()
        # iterates through namePart and concatenates sanitized characters into sanitized_name
        for j in range(len(namePart)):
            sanitized_char = namePart[i]
            # uppercases the starting letter of each namePart
            if j == 0:
                 sanitized_char = sanitized_char.upper();
            sanitized_name += sanitized_char # concatenation into sanitized_name
        # adds space into sanitized_name between nameParts
        if i < len(student_name_split):
            sanitized_name += " "
    
    return sanitized_name

def studentEmailIsValid(student_email):
    '''
    Checks if provided student_email is correct

    ...
    student_email must be alphanumeric, containing and must include a domain and hostname.
    This is done by splitting student_email by the @ symbol, and checking if the second index contains a period, with atleast 2 characters after it.

    Parameters
    ----------
    student_email : str
        A string containing the student email

    Returns
    -------
    True : bool
        if student_email is valid
    False : bool
        if student_email is invalid
    '''
    WHITELIST = "@."
    for char in student_email:
        if char not in WHITELIST or not char.isalnum:
            return False
    email_split = student_email.split("@")
    if len(email_split) != 2 or "." not in email_split[1] or email_split[1].index(".") >= len(email_split[1])-2:
        return False
    return True

def studentIDIsValid(student_id):
    '''
    Checks if student_id is valid by checking if it is alphanumeric.

    Parameters
    ----------
    student_id : str
        A string containing the student's id

    Returns
    -------
    True : bool
        if student_id is valid
    False : bool
        if student_id is invalid
    '''
    for char in student_id:
        if not char.isalnum():
            return False
    return True

def validatedInput(prompt, type="str"):
    '''
    Prompts the user, and double checks with them that they want the input.

    Parameters
    ----------
    prompt : str
        A String containing the message to be prompted to the user
    type : str
        A String to inform the method on what type of input we're looking for

    Returns
    -------
    userInput : str
        A String containing the input to be returned after validation
    '''
    userInput = ""
    confirmation = "n"
    while confirmation == "n" and confirmation != "y" and confirmation != "":
        userInput = input(prompt).strip()
        if (type == "int" and not userInput.isdigit()):
            print(f"{userInput} is not an integer.")
        elif (":" in userInput):
            print("Colons are not allowed.")
        elif (len(userInput) < 1):
            print("You did not enter anything.")
        else:
            confirmation = input(f"You entered: {userInput}. Proceed? Y/n: ")
            if (type == "int"):
                userInput = int(userInput)
    return userInput

def getIndexFromStudentID(student_id, db):
    '''
    Gets the index of specified student_id from the database by iterating through db and searching for student_id.
    This can be futher optimized, but I'm just using two pointer linear search for now.

    Parameter
    ---------
    student_id : str
        Id of student to delete
    db : list
        A list containing all entries for students
    '''
    for i in range(len(db)):
        if db[i]["id"] == student_id:
            return i
        if db[-i]["id"] == student_id:
            return len(db)+1-i
    return None

def queryStudentIDFromDB(student_id, db):
    '''
    Queries and returns student from specified DB file and student id, or returns type None if not found
    
    Parameters
    ----------
    student_id : str
        A String containing the student's ID
    db : list
        A list containing all entries for students
    
    Returns
    -------
    student_query : dict
        A dictionary object containing information about the queried student
    student_query : None
        None as no student was found
    '''
    student_query = None
    for student in db:
        if student_id == student["id"]:
            student_query = student
    return student_query

def populateDBfromFile(db_file):
    '''
    Populates a dictionary with the contents of the provided file directory
    
    Parameters
    ----------
    dir : str
        A string indicating the directory of the file to be read
    
    Returns
    -------
    db_populated : list
        A list containing all students within the file's contents, in dict form
    '''
    db_populated = []
    lines = db_file.readlines()
    
    for line in lines:
        line = line[:-1] # removes \n from the line
        line_split = line.split(":")
        student_temp = {
            "id": line_split[0],
            "name": line_split[1],
            "email": line_split[2],
            "semesters": {} ## semesters is structured as another dict as it allows multiple semesters to be contained within each student
        }
        # we know that semester and course info only appears starting from 4th index so we start from forth index.
        # we also know that it is listed as semesternum:courses, so we step by 2 to isolate the semester numbers
        semester_split = line_split[3:]
        for i in range(0, len(semester_split), 2):
            semester_num = semester_split[i] # gets semester number
             # we know that following a semester number, we have an array of courses (formatted num_of_courses[courses]) so we just substring and split by commas to get the courses
            semester_courses = semester_split[i+1][2:-1].strip().split(",")
            student_temp["semesters"][semester_num] = semester_courses # updating the semesters dictionary nested within the student to include the semesters.
        db_populated.append(student_temp)
    return db_populated

def addStudent(db):
    '''
    Function that guides user to adding a new Student to the database.
    Super long function (i hate it)

    Parameters
    ----------
    db : list
        A list containing all entries for students
    '''
    # student format
    student = {
                "id": None,
                "name": None,
                "email": None,
                "semesters": {},
            }
    
    # grabs student id from user
    while student["id"] == None:
        ## calls validatedInput method to validate if user input with user
        desired_id = validatedInput("Enter student's ID: ").upper()
        ## checks if studentid is valid before saving, or prompts user to re-enter
        if (studentIDIsValid(desired_id)):
            ## queries db to see if id already exists, prompts user to re-enter if already present in db, saves to student if not present yet
            if (queryStudentIDFromDB(desired_id, db) == None):
                student["id"] = desired_id
            else:
                print(f"Student already exists under id: {desired_id}.")
        else:
            print(f"{desired_id} is not alphanumeric.")

    while student["name"] == None:
        desired_name = validatedInput("Enter student's name: ")
        if studentNameIsValid(desired_name):
            student["name"]=desired_name
        else:
            print("Name is invalid. The name must be strictly alphabetical.")

    ## grabs student email from user
    while student["email"] == None:
        ## same idea as when grabbing id from the user. please look there.
        desired_email = validatedInput("Enter student's email: ").lower()
        if (studentEmailIsValid(desired_email)):
            student["email"] = desired_email
        else:
            print(f"{desired_email} is not an email.")

    semester_num = str(validatedInput("Enter student semester: ", "int")) # gets semester number from user

    # gets amount of courses in the semester, restricting course_amount to be within 1 and 5
    course_amount = 0
    while (course_amount < 1 or course_amount > 5):
        course_amount = validatedInput("Enter amount of courses in this semester: ", "int")
        if course_amount < 1 or course_amount > 5:
            print("Course amount can only be between 1 and 5.")
    courses = []

    # gets courses, following course_count
    course_count = 0
    while course_count < course_amount:
        desired_course = validatedInput(f"Enter course {course_count+1}: ").upper() # gets validatedInput from user for course code
        ## appends course amount to courses if valid
        if semesterCourseIsValid(desired_course):
            courses.append(desired_course)
            course_count+=1
        else:
            print("Course ID is not valid.")
    
    student["semesters"][semester_num] = courses ## adds new key:value pair containing semester_num and courses list inside of student["semesters"] dict
    # try except incase anything comes up. informs user that we couldn't add student if anything occurs.
    try:
        db.append(student)
        print("Record added successfully.")
    except():
        print("Failed to add record.")

def removeStudentById(db):
    '''
    Guides user through removing a student by their ID from the database

    Parameters
    ----------
    db : list
        A list containing all entries for students
    '''
    student_id_toRemove = validatedInput("Enter the student that you wish to remove's student ID: ")
    student_id_toRemove_idx = getIndexFromStudentID(student_id_toRemove, db) # looks for the index of the student
    ## if student index is found, then remove the student from db
    if student_id_toRemove_idx != None:
        # try except incase anything comes up. informs user that we couldn't remove student if anything occurs.
        try:
            db.pop(student_id_toRemove_idx)
            print("Successfully removed student.")
        except():
            print("Failed to remove student.")
    else: # else inform the user that the student could not be found / doesn't exist
        print("Student does not exist.")
        
def getStudentById(db):
    '''
    Guides user through querying a student's information by their ID from the database
    
    Parameters
    ----------
    db : list
        A list containing all entries for students
    '''
    student_id_toQuery = validatedInput("Enter the student ID that you wish to search for: ")
    student = queryStudentIDFromDB(student_id_toQuery, db) # attempts to find student
    ## if student found, print information
    if student != None:
        # try except incase anything comes up. informs user that we couldn't print the student if any errors occur.
        try:
            print(f"\nStudent ID: {student["id"]}\n"
                + f"Student Name: {student["name"]}\n"
                + f"Student Email: {student["email"]}\n"
                + f"Student Semesters:")
            for semester in student["semesters"]:
                print(f"Semester {semester}: {student["semesters"][semester]}")
        except():
            print("Could not print student.")
    else: # else inform user that student doesn't exist / can't be found
        print("Student does not exist.")

def writeDBtoFile(db, db_file):
    '''
    Writes contents of db into specified db_file. Overwrites everything.

    Parameters
    ----------
    db : list
        A list containing all entries for students
    db_file : file
        File pointing towards the database's file
    '''
    ## iterates between each student in db
    for student in db:
        write_string = f"{student["id"]}:{student["name"]}:{student["email"]}:" # uses fstrings to format write_string
        semesters_processed = 0 # keeps track of how many semesters we've processed, so we can know when or when not to add another colon
        # iterates between each semester within the student
        for semester in student["semesters"]:
            write_string += f"{semester}:{len(student["semesters"][semester])}[" # adds the semester to the write_string using fstrings again
            # enumerates and iterates between each course in the semester
            for i in range(len(student["semesters"][semester])):
                write_string += student["semesters"][semester][i] # adds the course into semester
                # if there's still more courses, add a comma
                if i < len(student["semesters"][semester])-1:
                    write_string += ","
            write_string += "]" # finish off the semester's course list with a closing square bracket
            semesters_processed += 1 # increments semesters processed, so we know we processed another semester
            # if there's more semesters present, add another colon
            if semesters_processed < len(student["semesters"]):
                write_string += ":"
        write_string += "\n" # newline so we can add another student in the list
    
    # tries writing write_string to the file. prompts user with success status.
    try:
        db_file.write(write_string)
        print("Sucessfully written to database file.")
    except():
        print("Could not write to database file.")

def main():
    '''
    Main function. Executed on startup.
    '''    
    ## loads database into db variable
    db_dir = input("Enter path to student database (default is ./studentDatabase.dat): ")
    if (db_dir == ""):
        db_dir = "./studentDatabase.dat"
    db_file = open(db_dir, "r+")
    db = populateDBfromFile(db_file)
    
    willExit = False
    
    while (not willExit):
        print("Student Database Manager Client\n"
              + "a: add student\n"
              + "e: edit information from existing student\n"
              + "d: drop a course from an existing student\n"
              + "r: remove records of existing student\n"
              + "i: get information from existing student\n"
              + "q: exit\n")
        userAction = input("Enter desired action: ").lower()
        
        if (userAction == "a"):
            addStudent(db)
        elif (userAction == "e"):
            print("WIP")
        elif (userAction == "d"):
            print("WIP")
        elif (userAction == "r"):
            removeStudentById(db)
        elif (userAction == "i"):
            getStudentById(db)
        elif (userAction == "q"):
            writeDBtoFile(db, db_file)
            willExit = True
        else:
            print(f"{userAction} is not a valid action. Please try again.")
        
        print()

main()
exit(0)