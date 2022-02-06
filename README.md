# Rainbow 
The Rainbow programming language is a simple language that aims
to be powerful and stocked with features. It has only 3 data types
namely:
- int (for integers eg. 109, 89 etc.)
- decimal (for floating point numbers eg. 3.14 , 7.84) 
- string (for storing text eg. Hello World)

## Statements 
As mentioned above a Rainbow lang program is a collection of statements having a definite purpose. Each Rainbow program is written in a file having a .rbow extension. It consists of one or more statements that perform a fixed set of actions.
Each `statement` has the following syntax: \
``
STATEMENT-TYPE ARGS
``

Here, "STATEMENT-TYPE" refers to any statement type which describes what that statement actually does and "ARGS" refer to arguements that are given to the keyword. For example :\
``
Set int a 4
``

Here the statement type is "Set" (i.e it is a statement that sets the value of a variable) and everything following it are the arguements
to Set. This statememt basically initializes a variable by the name 'a' to the value 4. One more thing to note about Rainbow's statements are that they must not span to 2 lines i.e each statement must end in a single line because the language assumes that each line is one single statement.

### Statement types 
The Rainbow language has the following 6 types of statements:
- `Set`: As mentioned above, a set statement either declares a new 
variable or changes the value of an already existing variable depending on the arguements passed to it. For example:\
``
Set int a 12
``
This creates a new variable of type `int` called a and initializes to the value 12 
``
Set a 32
``

This changes the value of an already declared variable `a` to 32. Notethat this syntax will throw an error if the variable whose value you are trying to change does not exist . In this example , if a has not been declared then the error message will state `the symbol a is undefined in the current context`
