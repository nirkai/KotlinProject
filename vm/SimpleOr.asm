@256
D=A
@0
M=D
@7
D=A
@0
A=M
M=D

D=A
D=D+1
@0
M=D
@5
D=A
@0
A=M
M=D

D=A
D=D+1
@0
M=D
@SP
A=M-1
D=M
A=A-1
D=D-M
@IF_TRUE0
D;JEQ
D=0
@SP
A=M-1
A=A-1
M=D
@IF_FALSE0
0;JMP
(IF_TRUE0)
D=-1
@SP
A=M-1
A=A-1
M=D
(IF_FALSE0)
@SP
M=M-1

@5
D=A
@0
A=M
M=D

D=A
D=D+1
@0
M=D
@5
D=A
@0
A=M
M=D

D=A
D=D+1
@0
M=D
@SP
A=M-1
D=M
A=A-1
D=D-M
@IF_TRUE1
D;JEQ
D=0
@SP
A=M-1
A=A-1
M=D
@IF_FALSE1
0;JMP
(IF_TRUE1)
D=-1
@SP
A=M-1
A=A-1
M=D
(IF_FALSE1)
@SP
M=M-1

@SP
A=M-1
D=M
A=A-1
D=D+M
@IF_TRUE3
D;JLT
D=0
@SP
A=M-1
A=A-1
M=D
@IF_FALSE3
0;JMP
(IF_TRUE3)
D=-1
@SP
A=M-1
A=A-1
M=D
(IF_FALSE3)
@SP
M=M-1
