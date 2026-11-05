 Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.


## Week 6 Code Review
I have read every line of production code currently in the main branch.

1. Magic numbers: Your code uses magic numbers like 8 in, for instance, Board. Uncle Bob talks about not using it for better readability. So you would want to use `private static final ROWS = 8;`. Later when you set up Checkstyle, there is a rule you can add to your config file to automatically check these. See here: https://checkstyle.sourceforge.io/checks/coding/magicnumber.html
2. Consistent comments: Your Game class has a class comment, `/** A chess game: owns the board and tracks whose turn it is. */`. You want to be consistent acorss the project about whether to have these.
3. More on comments: People can probably argue that `// Swing components must be created on the event dispatch thread.` in the main method is not necessary. Hmm, your call.

Otherwise, the code looks good to me!
