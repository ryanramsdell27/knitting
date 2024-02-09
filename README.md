# Knitting Tools
This project aims to be a collection of tools for testing and analyzing knitting patterns.

## Grammar and Parsing
The grammar defined here is designed to be similar to what is used in existing knitting patterns. The parser generated to match the specification of this grammar is then able to convert a written pattern into some code representation during the interpretation phase.

At the highest level there are expressions of four different forms.
### Basic stitches
- Knit stitches `(knit|k)[0-9]+`
- Purl stitches `(purl|p)[0-9]+`
- Casting on `(cast on|caston|co)[0-9]+`
### Decreases
`(knit|k|purl|p)[0-9]+(tog|together)[:(\w)]` where `[:(\w)]` is an optional, one word qualifier matching a valid decrease type
### Increases
`(make|m)[0-9]+(knitwise|knit|k|purlwise|purl|p)[:(\w)]` where `[:(\w)]` is an optional, one word qualifier matching a valid increase type
### Repeats
`(expression*)[0-9]+` Any valid chain of the expression types defined here, possibly nested, to be repeated some number of times


## Interpretation and API
Once the input has been parsed, it needs to be interpreted. The interpretation step converts the tokenized input to a tree, which is traversed, mapping each node to a part of the knitting API.

Implementations of this API can choose to build a knitted object as a set of stitches or some other data structure, or perform analysis without an underlying representation of the pattern as it is being built. Two are included in this project, one that builds the graph of linked stitches, and another that simply counts stitches as they are made.

### API

- _**void init(int numStitches, boolean join);**_
Initial cast on of a project. It takes the number of stitches to initially cast on and whether to join in the round. The join param is still in process, the parser currently does has no mechanism for identifying joins, and all implementations of this API default to being in the round.

- _**Stitch basicStitch(StitchType type);**_

    Performs a stitch operation of StitchType type onto the current knitting pattern, ie. knit, purl, cast on. It will return the stitch object it performed by the pattern

- _**void decrease(StitchType type, int num, DecreaseType decreaseType);**_

    Perform a decrease that combines num stitches together, returning the new stitch of StitchType type.

- _**void increase(StitchType type, int num, IncreaseType increaseType);**_

  Perform an increase of num stitches, returning the last stitch created in sequence.

## Visualization
Once a pattern has been parsed and interpreted into some data structure, it can then be visualized.

There is still quite a lot to do here. The current linear time solution is to map stitches to coordinates in 3d space by estimating where they would be based on which row/round in the pattern the stitch is, and how many stitches are in a round. This will place each stitch on a circle with a radius determined by the number of working stitches. This is not very accurate for anything besides tubes.

An alternate solution is through constructing a dissimilarity matrix based on taxicab distance along the surface of the knitted object (with discrete points represented by stitches and distances determined by stitch type). This matrix is constructed by finding the shortest paths between each set of points, and then through multidimensional scaling (aka principal coordinate analysis) it can estimate coordinates in euclidean space.


# How to run
## Building
Use gradle `gradlew build`

### JavaCC Gradle Plugin
The knitting parser is built with JavaCC using the [JavaCC gradle plugin](https://github.com/javacc/javaccPlugin) which handles compiling the `.jj` source files and copying generated `.java` source to the right directory.

You can build the parser alone via `gradlew compileJavacc`, the generated source is placed in `build/generated`.

## Main Application
Build the application

Run the app with `gradlew run`

Alternatively, the main method can be found in `dev.ryanramsdell.Main`, run this application as you would any other java app

## Python visualizer
- Set up your python environment. Consider using [venv](https://docs.python.org/3/library/venv.html) in the `mds` directory
- Install the necessary modules with `pip install -r requirements.txt`
- Run the main java application to generate `mds/out.py`
- Run `python mds/test.py` to display the output
