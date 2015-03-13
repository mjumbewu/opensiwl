# About #

This project is a kind of reimplementation of the Sprint Instinct Widget Library meant to capture some of the features often desired by Instinct developers.  Additional features that will be supported are:

  * Layout Managers -- so that placing stuff on a frame or panel just looks right (don't have to worry long about positioning and such).  So far there are:
    * Linear layout manager for arranging widgets in a row or column
    * Absolute layout manager for arranging widgets at explicit coordinates
    * It would also be nice to have a Grid layout (which I should get to in the foreseeable future) and a Flow layout (which is less foreseeable).
  * Orientability -- (only for frames)
  * Static Text -- just as there are static images, we should be able to easily display text on the screen ... and not have it limited to 3 lines
  * Panels -- like in the browser and Photo app that allow you to place widgets along the edges of the screen
  * Flickability -- frames and widgets should be able to do something useful with flick events
  * Full widget set

Tentative features:

  * Theming(?) -- ever-elusive and ever-desired.  Not sure how I'd implement yet.  Suggestions?

This project is still new.  Don't hesitate to try out some of the tests or download the library.  Requests/comments/bugs?  Please, create an issue.  Want to get involved, hit up my username at gmail.com.

# Tests #
### 10 Mar 09 ###
Uploaded two more SIWLExtensions test. These ones are to check the StaticText widget and the padding on LinearLayouts. I need to improve test coverage :).

### 09 Mar 09 ###
Uploaded a functional <a href='http://code.google.com/p/opensiwl/downloads/detail?name=SIWLExtensionTests.zip&can=2&q=#makechanges'>test suite</a> for a new portion of this project that I've started (see <a href='http://oiwl.blogspot.com/2009/03/hard-drive-issues-and-siwl-extensions.html'>this blog post</a> for more info) to extend the SIWL in addition to making a better toolkit.  The suite only has one test in it right now; the <a href='http://code.google.com/p/opensiwl/source/browse/trunk/SIWLExtensionTests/src/TestFrame1.java'>source code</a> of the test (with its lack of positioning instructions) is more interesting than the actual test.  Check it out!

### 03 Mar 09 ###
New tests--coming soon!  I meant to have updated tests/demos up, but seeing as how I spent the last two days sleeping in airports while trying to get back to Philly, it didn't happen.

### 28 Feb 09 ###
I uploaded a small test suite (3 tests right now) that I will continually add to.  Just click a phone button to proceed to the next test.