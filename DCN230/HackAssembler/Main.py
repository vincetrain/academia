from Parser import Parser
FILEPATH = "testfile.asm"

parser = Parser(FILEPATH)
while(parser.hasMoreLines()):
    parser.advance()
    print(f"Current line: {parser.currentInstruction}")
    print(f"Instruction type: {parser.instructionType()}")
    print(f"Has more lines? {parser.hasMoreLines()}")