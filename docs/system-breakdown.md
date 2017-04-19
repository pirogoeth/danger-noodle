Breakdown
=========

This document breaks down the execution flow through the [`danger-noodle`](https://github.com/pirogoeth/danger-noodle) intepreter.

The overall flow is fairly straight forward:

```
	Lexer -> Parser -> Evaluator
```

Each component of the flow is built to be modular - lexer only handles token retrieval and creation,
parser only handles the structuring of syntax nodes, and evaluator runs the syntax nodes in the
appropriate scope.

Each part can function by itself, if given the proper inputs.


----


Lexer (aka Scanner)
===================

The lexer will take an input file and turn it in to classified tokens that can be easily turned into
a syntax node.

For example:

```
String name = "Hugh";
print("Hello", name, "!");
```

Becomes:

```
#	PRIM		SUB				TOKEN
	CONTROL	DECLARE		String
	OPERAND	IDENTIFIER	name
	OPERATOR	-				=
	OPERAND	STRING			Hugh
	SEPARATOR	-				;

	FUNCTION	BUILTIN		print
	SEPARATOR	-				(
	OPERAND	STRING			Hello
	SEPARATOR	-				,
	OPERAND	IDENTIFIER	name
	SEPARATOR	-				,
	OPERAND	STRING			!
	SEPARATOR	-				)
	SEPARATOR	-				;
```

A list of these tokens is then returned to the entry-point and is passed in to the parser.


----


Parser
======

The parser takes a plain list of tokens and turns them in to something easier to execute.
Our parser generates a syntax node for each statement it can find.

For example, if the parser is given this input:

```
  FUNCTION	BUILTIN		print
  SEPARATOR	-				(
  OPERAND	STRING				Hello
  SEPARATOR	-				,
  OPERAND	IDENTIFIER		name
  SEPARATOR	-				,
  OPERAND	STRING				!
  SEPARATOR	-				)
  SEPARATOR	-				;
```

This syntax tree is generated:

```
Statement ~~>
  Expression ~>
    FunctionCall ~>
      Identifier ~> `print`
        TOKEN :: [test.hb:2:0]: Token `print` P:[FUNCTION] S:[BUILTIN]
      Args ~>
        Expression ~>
          Primitive ~> `Hello`
            TOKEN :: [test.hb:2:6]: Token `Hello` P:[OPERAND] S:[STRING]
        Expression ~>
          Identifier ~> `name`
            TOKEN :: [test.hb:2:15]: Token `name` P:[OPERAND] S:[IDENTIFIER]
        Expression ~>
          Primitive ~> `!`
            TOKEN :: [test.hb:2:21]: Token `!` P:[OPERAND] S:[STRING]
```

While this style of parsing may seem non-traditional for an interpreter and more suited to a compiler, it makes
contextual evaluation much simpler. You can see an example of a similar style output from `clang` / `LLVM` during the parse phase:

![media-S19230](file://media/596193302.png)


The parser has a single public instance method which is used to start the parsing process: `parse()`.

Creating and using a parser instance is simple because of the modularity of the interpreter system:

```
List<Token> tokens = ...;
Parser p = new Parser(tokens);
while ( p.canParse() ) {
  Statement stmt = p.parse();
```

The parser attempts to be as efficient as possible with regard to how much context will be handled within a parser unit.
The parser will often recurse while processing a statement to process another part of the statement.

For example, flow of the above `print` call through the parser strongly resembles the syntax tree it generates:

```
# Initial call
parse() ->

  # pop statement always eats the following semicolon
  func_call_stmt <- pop statement: `print("Hello", name, "!")`

  func_call <- parseFunctionCall(func_call_stmt) ->
    func_handle <- pop token - `print`

    # current statement stack: `("Hello", name, "!")`
    func_handle is_valid?

    # function handle will always be followed by a MATCHING set of parens!
    eat matching parens!

    # current statement stack: `"Hello", name, "!"`
    # now, we will build a list of expressions - arguments to the function
    # (in the code, a loop builds the argument expressions, but here, that is omitted for simplicity)
    args_list ++ parseExpression(pop until ',' or ')' - `"Hello"`) -> Primitive[String] `Hello`
    args_list ++ parseExpression(pop until ',' or ')' - `name`) -> Identifier `name`
    args_list ++ parseExpression(pop until ',' or ')' - `"!"`) -> Primitive[String] `!`

    # after last `pop until`, parser found the end of the statement.
    ret <- FunctionCall(handle = func_handle, args = args_list)

  ret <- Statement(expr = Expression(function_call = func_call))
```

## To be continued...


----

Evaluator
=========

## To be continued...