grammar Asm;

program: statement+ ;
         
statement
    : label
    | nullaryInst // no operands - ret, nop, cli, ...
    | unaryInst   // one operand - inc, neg, ...
    | binaryInst  // complex operands - add, sub, xor, ...
    | segInst     // things that be done with segment registers
//    | callInst
//    | jmpConditional
//    | jmpAbsolute
//    | shift       // arg can only be 1 or cl
//    | inInst
//    | outInst
//    | interrupt

    | loadAddress // lea, les, lds all follow same format
                    // (ptr size is different, but parser doesn't need to care
                    //   b/c it's determinable based on the instruction)
    | ret         // odd b/c it requires info about labels
                    // (separate not b/c of parser but so that tree walker
                    //   will see it differently)
//    | coprocessor // esc
    ;

label: ID ':' ;

nullaryInst: nullaryMnemonic ;
nullaryMnemonic
    : 'AAA' 
    | 'AAD'
    | 'AAM'
    | 'AAS'
    | 'CBW'
    | 'CLC'
    | 'CLD'
    | 'CLI'
    | 'CMC'
    | 'CWD'
    | 'DAA'
    | 'DAS'
    | 'IRET'
    | 'LAHF'
    | 'LOCK'
    | 'LODSB'
    | 'LODSW'
    | 'MOVSB'
    | 'MOVSW'
    | 'NOP'
    | 'POPF'
    | 'PUSHF'
    | 'REP'
    | 'REPE'
    | 'REPNE'
    | 'SAHF'
    | 'SCASB'
    | 'SCASW'
    | 'STC'
    | 'STD'
    | 'STI'
    | 'STOSB'
    | 'STOSW'
    | 'WAIT'
    | 'XLAT'
    ;
          
unaryInst: unaryMnemonic unaryOperand ;
unaryMnemonic
      : 'DEC'
      | 'DIV'
      | 'INC'
      | 'IDIV'
      | 'IMUL'
      | 'MUL'
      | 'NEG'
      | 'NOT'
      ;

unaryOperand
    : reg8
    | reg16
    | 'BYTE' memory
    | 'WORD' memory
    ;

binaryInst
    : binaryMnemonic binaryOperands
    ;
binaryMnemonic
    : 'ADC'
    | 'ADD'
    | 'AND'
    | 'CMP'
    | 'MOV'
    | 'OR'
    | 'SBB'
    | 'SUB'
    | 'TEST'
    | 'XCHG'
    | 'XOR'
    ;

binaryOperands
    : reg8 ',' reg8
    | reg8 ',' memory
    | reg8 ',' imm8
    | memory ',' reg8
    | memory ',' 'BYTE' imm8
    | reg16 ',' reg16
    | reg16 ',' memory
    | reg16 ',' imm16
    | memory ',' reg16
    | memory ',' 'WORD' imm16
    ;

loadAddress : leaMnemonic leaOperands ;
leaMnemonic
    : 'LEA'
    | 'LES'
    | 'LDS'
    ;
leaOperands
    : reg16 ',' memory
    ;

memory
    : (seg16 ':')? '[' (seg16 ':')? ptr ']'
    ;
ptr
    : imm16 (('+'|'-') imm16)* ('+' base16)? (('+'|'-') imm16)* ('+' idx16)? (('+'|'-') imm16)*
    | imm16 (('+'|'-') imm16)* ('+' idx16)? (('+'|'-') imm16)* ('+' base16)? (('+'|'-') imm16)*
    | base16 (('+'|'-') imm16)* (('+'|'-') imm16)* ('+' idx16)? (('+'|'-') imm16)*
    | idx16 (('+'|'-') imm16)* ('+' base16)? (('+'|'-') imm16)*
    ;

segInst
    : segMnemonic segOperands
    ;
segMnemonic
    : 'MOV'
    ;
segOperands
    : seg16 ',' reg16
    ;

ret
    : 'RET'
    ;

reg8
    : 'AL'
    | 'AH'
    | 'BL'
    | 'BH'
    | 'CL'
    | 'CH'
    | 'DL'
    | 'DH'
    ;

reg16
    : 'AX'
    | 'BX'
    | 'CX'
    | 'DX'
    | 'SI'
    | 'DI'
    | 'SP'
    | 'BP'
    ;

base16
    : 'BX'
    | 'BP'
    ;
idx16
    : 'DI'
    | 'SI'
    ;

seg16
    : 'SS'
    | 'CS'
    | 'DS'
    | 'ES'
    ;

imm8
    : DECDIGITS
    | HEX8      
    ;

imm16
    : ID
    | DECDIGITS
    | HEX8
    | HEX16
    ;

DECDIGITS: [0-9]+ 'D'? ;
HEXDIGITS: [0-9A-F] ;
HEX8: '0x'? '0'? HEXDIGITS HEXDIGITS? 'H'? ;
HEX16: '0x'? '0'? HEXDIGITS HEXDIGITS? HEXDIGITS? HEXDIGITS? 'H'? ;
ID: [A-Z_\.] [A-Z0-9_\.]* ;
COMMENT: ';' ~['\n']* '\n' -> skip ;
WS: (' '|'\n')+ -> skip ;