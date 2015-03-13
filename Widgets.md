These are some thoughts that I've been having about Widgets.

**Widget**:

  * Can tell you its size and position (but can only control its minimum size; is otherwise passively positioned)
  * Knows what it looks like (i.e. can draw itself for you on a Graphics context) in different states
  * Can tell you its possible states and current state (but does not control its state)
  * Can choose to not interact with the user (i.e. be disabled) (?)

So, even though Widgets seem to be what the user interacts with, they are largely passive objects having control only over their minimum sizes, their appearance, and whether they are interactive (though even having control over whether they're interactive might be a stretch).  In other words, Widgets are like puppets, not agents.  You push them and they move.  They may move other Widgets as well, but it is purely coincidental (Widget dynamics, if you will).

Widgets have managers to help push them around and make them seem active.  One of these managers is called a Layout.  A Layout is actually a Widget as well, but has the added ability of determining the positions and sizes of other Widgets, to the extent that this is possible.

**Layout**:

  * Can manage the size and position of one or more widgets

Widgets may also have event managers called Automatons (or automata, whatever).  These are essentially state machines; they transition the Widget from one state to another depending on the type of user interaction and whatever other relevant information is stored in the Widget (i.e. "on the tape").  _NOTE: In the current implementation, the Widgets themselves are their own state machines.  I think I am comfortable with this design._

**Automaton**:

  * Can be notified of events (both program- and user-initiated)
  * Can set the widget state