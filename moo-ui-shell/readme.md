# Moo Shell
=====================
Console (text) based UI for Moo. This is a default Moo UI. Powered by 
two components: reader and writer.

## Why reader and writer?
---------------------
Without advanced text UI such as curses, which natively is not available to 
Java, it is near impossible to control input and output in a synchronized 
way. Imagine typing a message into System.in and getting overriden by incoming 
message from System.out. That's really the bulk of the problem. Separating 
chat reading and writing into their own applications not only solves the 
problem, but allows for additional flexibility. For instance, if one wants 
to only observe public chat messages, there is no need for writing 
functionality, so writer does not need to be started.