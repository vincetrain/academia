class Parser:

    def __init__(self, filepath):
        file = open(filepath, "r")
        self.lines_clean = []
        self.line_idx = -1

        lines_dirty = file.readlines()

        # iterate through lines_dirty and add accepted lines to lines_clean
        for line in lines_dirty:
            c_line = line.strip()
            # filter out comments, or empty lines
            if len(c_line) > 0:
                if line[0:2] != "//":
                    self.lines_clean.append(c_line.split("//")[0].strip())

    def advance(self):
        self.line_idx += 1
        self.currentInstruction = self.lines_clean[self.line_idx]

    def hasMoreLines(self):
        return self.line_idx < len(self.lines_clean)-1
    
    def instructionType(self):
        currentInstruction_type = "null"
        if (self.currentInstruction[0] == "@"):
            currentInstruction_type = "A_INSTRUCTION"
        elif (self.currentInstruction[0] == "("):
            currentInstruction_type = "L_INSTRUCTION"
        else:
            currentInstruction_type = "C_INSTRUCTION"
        return currentInstruction_type