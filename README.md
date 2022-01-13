# How to use

Load the project into your preferred IDE and execute it there.

Alternatively, if you have maven installed, you can execute `mvn package` in the code directory and then execute the resulting .jar file in the "target" directory.

On the main screen, you will see a collection of all available robots in the left panel. (All robots in the de.visparu.spacerobots.game.entities.external.robots package)<br/>
To add one of them to the arena, click on it's symbol. One robot can be present multiple times. To remove a robot from the selection, just click on it in the right panel.<br/>
When you are done selecting your robots, click on "Start" to start a match.

# How to extend

Duplicate one of the preview classes in the de.visparu.spacerobots.game.entities.external.robots package.<br/>Alternatively, you can also create a new class in that package which **extends the Robot class**.

Then, write your initialization logic in the initialize() method and your runtime logic in the update() method.

If you are using a sufficiently modern IDE (e.g. Eclipse, IntelliJ, NetBeans), you can access the methods available to you via Intellisense by typing "super." in your update() method.<br/>All methods should have sufficient documentation attached to them.