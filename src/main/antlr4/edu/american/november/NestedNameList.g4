// Derived from code for Language Implementation Patterns
// http://pragprog.com/book/tpdsl/language-implementation-patterns
// Original source code can be downloaded from
// http://pragprog.com/titles/tpdsl/source_code
grammar NestedNameList;

list : '[' elements ']' ; // match bracketed list
elements : element (',' element)* ; // match comma-separated list 
element : NAME | list ; // element is name or nested list 
NAME : ('a'..'z' |'A'..'Z' )+ ; // NAME is sequence of >=1 letter
