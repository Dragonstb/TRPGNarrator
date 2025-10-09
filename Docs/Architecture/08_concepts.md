# Concepts

## Logging

- Messages have the format "A text message (a code)"
- The code is a leading 'C', 'S', or 'V' depending if the exception was thrown by the client part, the server part, or the virtual host part
of the application, respectively, followed by a 5 digit number. Example: C00123.
- Each code is used only once in the code, so that we can easily find the location of the exception. In case several exceptions can be
very close by in the application code, they may share the same exception code. Example: Input validation at the beginning of a method.
