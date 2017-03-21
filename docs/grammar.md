formal grammar for danger-noodle
================================

this document will describe the prefix grammar we use for danger-noodle
in a bottom-up form, describing the most basic pieces of a expression first,
working up to the most general case statement.

danger-noodle is a prefix-style language based on havabol semantics. the
style used for defining this grammar is semi-BNF notation with regular expression
style quantifier notations.

```
@created 19 Mar 2017
@author Sean Johnson <isr830@my.utsa.edu>
@revision 1
```

----

## legend

`[...]` denotes a selection set - one of any of the contained elements can be
        matched as the LHS.

`i ~> j` denotes a contiguous range of code points in the ASCII sequence,
         unless otherwise noted.

`n | m` denotes a "choice", which may be chained to create a selection set of
        non-contiguous ranges.

`c?` denotes an optional rule. the preceding rule or literal will be optional
     in the implementation.

`char(n)` denotes the character value of a given value inside `n`, which may be
          a single character, character set, or range.

`set[^c]` denotes an exclusion from a known character set. ex., "ascii[^char(0x22)]"
          represents the `ascii` character set without the `char(0x22)` (`"`) value included.

`set <~ [a, ...]` denotes required bounds that will be used on a set rule.
                  ex., "string' <~ [excl]" shows that the `string'` will accept one bound,
                  `excl` which can be used to define a set exclusion (`set[^excl]`).

`"..."` denotes a literal.

`; ...` denotes a comment inside the grammar.


Quantifiers come from the [PCRE Regex Rules (2)](http://www.regular-expressions.info/pcre2.html).

----

## special definitions

```
; "normal" character definitions
<alpha> ::= [a-zA-Z]
<digit> ::= [0-9]
<alnum> ::= [ <alpha> <digit> ]
<space> ::= char(" ")
<tab>   ::= char("\t")
<eol>   ::= char("\n")
<punct> ::= [ "!" "\"" "#" "$" "%"
              "&" "'" "(" ")" "*"
              "+" "," "." "-" "/"
              ":" ";" "<" "=" ">"
              "?" "@" "[" "]" "\\"
              "^" "_" "`" "{" "|"
              "}" "~" <space> <tab>
              <eol>
            ]
<blank> ::= [ <space> <tab> ]
<white> ::= [ <blank> <eol> ]

; ascii code-based definitions
<ascii> ::= char(0x0 ~> 0x7F)

<ascii_ctrl>  ::= char(0x0 ~> 0x1F | 0x7F)
<ascii_punct> ::= char(0x20 ~> 0x2F | 0x3A ~> 0x40 | 0x5B ~> 0x60 | 0x7B ~> 0x7E)
<ascii_alpha> ::= char(0x41 ~> 0x5A | 0x61 ~> 0x7A)
<ascii_digit> ::= char(0x30 ~> 0x39)
<ascii_space> ::= char(0x20)
<ascii_tab>   ::= char(0x09)
<ascii_eol>   ::= char(0x0A)
<ascii_blank> ::= [ <ascii_space> <ascii_tab> ]
<ascii_white> ::= [ <ascii_blank> <ascii_eol> ]
```

----

## language primitives

```
; signed, two's-complement 32-bit integer (danger-noodle type Int)
; integer primitive max (2^31)-1, min (-2^31)
positive_integer ::= <digit>+?
negative_integer ::= "-" positive_integer

integer ::= positive_integer
          | negative_integer

; double-precision 64-bit IEEE 754 floating point (danger-noodle type Float)
; float primitive max and min see [1]
positive_float ::= <digit>*? "." <digit>+?
negative_float ::= "-" positive_float

float ::= positive_float
        | negative_float

; mutable n-byte character string (danger-noodle type String)
; arbitrary size limited by working memory
string  ::= "\"" string'[^char(0x22)] "\""
          | "'" string'[^char(0x27)] "'"
          | "`" string'[^char(0x60)] "`"

string' <~ [excl]
        ::= <ascii[excl]>*?
          | Ø

; single-byte boolean value
boolean ::= T
          | F

; general expression chunk encapsulating number types
number ::= integer
         | float

; XXX - should we actually introduce a nil value???
null_val ::= nil

; all primitive values
primitive ::= number
            | string
            | boolean
            | null_val
```

----

## language construct definition

```
; built-in datatypes
datatype ::= Int
           | Float
           | String
           | Bool

; comment line construct
comment ::= "//" <ascii[^char(<ascii_eol>)]> <ascii_eol>

; variable identifiers
; can only begin with an alphabetic character or underscore
ident ::= "_" ident'
        | <alpha> ident'

ident' ::= <alnum>*?

; prefix, Lisp-style - declaration statements
; a declaration in this style will also return the variable handle
; which provides the ability to have a compound declaration/initialization expression
; very inspired by golang.
; ex., `(decl testInt Int)`
;      `(decl testInt []Int)`
declaration ::= "(" <blank>* "decl" <blank>+ ident <blank>+ "[]"? datatype <blank>* ")"

```

----

## language operations definition

```
; subscript definitions
subscript ::= "[" <blank>* ident <blank>* integer+ <blank>* "]"

```

----

## operators definitions

```
binary_operator  ::= "+"
                   | "-"
                   | "*"
                   | "/"
                   | "^"
                   | "#"
                   | ">"
                   | "<"
                   | ">="
                   | "<="
                   | "!="
                   | "=="
                   | "and"
                   | "or"

binary_operation ::= "(" <blank>* binary_operator <blank>+ statement <blank>+ statement <blank>* ")"

unary_operator   ::= "-"
                   | "not"

unary_operation  ::= "(" <blank>* unary_operator <blank>+ statement <blank>* ")"

```

----

## expression definitions

```
func_call  ::= "(" <blank*> ident <blank>+ func_args <blank>* ")"
func_args  ::= expression func_args'
func_args' ::= <blank>+ func_args
             | Ø

; An expression is anything that returns
expression ::= func_call
             | binary_operation
             | unary_operation
             | subscript
             | primitive
```

----

## initializers

```
; prefix-style initializer
; ex., (let someVar "string primitive")
;      (let someAry {1, 2, 3, 4, 5})
;      (let someVal nil)
;      (let someVal (func someAry someVar))
;      (let (decl ary []Float) { 1.0, 2.0, .30, .40 })
initializer  ::= "(" <blank>* "let" <blank>+ accessor <blank>+ initializer' <blank>* ")"
initializer' ::= ary_literal
               | expression

; accessor for use inside initializers
; ex., (let var ...)
;      (let [someAry 1] 12)
;      (let (decl thing Int) 12.0)
accessor     ::= ident
               | subscript
               | declaration

; array literal
; ex., { 1, 2, 3, 4, 5 }
ary_literal  ::= "{" ary_element "}"
ary_element  ::= <blank>* primitive <blank>* ary_element'
ary_element' ::= "," ary_element
               | Ø
```

----

## statement definition

```
; a statement is the highest-level, most encapsulating parse target.
; any previous part of this grammar should be able to reduce to a statement.
statement  ::= initializer
             | assignment
             | expression
```

----
# footnotes

    [1]: https://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.2.3 "JavaSE IEEE 754 Floating Point: Format and Values"
    [2]: http://www.regular-expressions.info/refrepeat.html "PCRE Regex Rules"
