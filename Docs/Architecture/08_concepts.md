# Concepts

## Data exchange

### Ingame: from client to virtual host

- Any request ingame from client to virtual host is encapsuled in a command object. The virtual host provides one interface method for
receiving this command.
- The virtual host deciphers the command an knows how to interpret it.
- The server just delivers the command to the virtual host without knowing what the command is or means.
- Waived alternative: One interface method for each service task the virtual hosts offer. The server would have either to decipher the
command from the client for calling the correct method of the virtual host, or offer one api endpoint for each interface method. In either
way, any new command added to the capabilities of a virtual host would require an adaption of the server code which is not needed with the
chosen solution of the command object.

## Logging

- Messages have the format "A text message (a code)"
- The code is a leading 'C', 'S', or 'V' depending if the exception was thrown by the client part, the server part, or the virtual host part
of the application, respectively, followed by a 5 digit number. Example: C00123.
- Each code is used only once in the code, so that we can easily find the location of the exception. In case several exceptions can be
very close by in the application code, they may share the same exception code. Example: Input validation at the beginning of a method.
