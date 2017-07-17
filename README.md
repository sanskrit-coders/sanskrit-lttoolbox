
# Intro
## LTToolbox context
- One can write *finite state transducers* for natural language processing (translation, declension, analysis) using lt-toolbox technologies.
- One creates dix files defining these tranducers, compiles them to bin files and then uses them for the final ends.
- Lt-toolbox libraries and tools are available in C++ and java.

## Goals
- Write scala/ java wrappers for invoking FST-s in lt-toolbox bin files.

# SCL tools
## Intro
- Smt Ambaa KulkarNi has led the creation of several such FST based tools. These are very useful for word generation and analysis.
- They're hosted on <scl.samsaadhanii.in> and mirrors.

## Resources available as of July 2017:
- Only the lttoolbox compatible bin files are provided without any documentation (*by request*, not on any public site).
- This is usually a part of the scl website code, which is antiquated and mostly useless for further development (while still serving as a useful reference for how the core lttoolbox bin files are to be invoked) as it:
  - relies on CGI technology.
  - is written in perl.
  - has a poor build system.
- These lttoolbox compatible bin files don't work with the Java libraries as of July 2017 - see [thread](https://sourceforge.net/p/apertium/mailman/apertium-stuff/?viewmonth=201706).
- Smt Ambaa does not provide the dix files (including to this author).
- So there is an outsider cannot consider using them :
  - to grok how the bin files are to be invoked
  - to understand the underlying lttoolbox technology by example.
  - to develop the FST-s further.

## Invocation tips
The below are sourced mostly from communication with smt ambaa and from experiments.

### General tips
- "To know the input for tin / krt / taddhita generator, you give a tinanta / krdanta / taddhitaanta to the all_morf.bin, and it will produce the analysis. That analysis is the input for the generator."

### subanta generator
- Regarding the level parameter:
    - level 1 is used for the avyutpanna praatipadikas and dhaatus (corresponds to inflectional morphology, with sup and tin)
    - level 0 is for the vyutpanna praatipadikas.
    - level 2: vyutpanna krdanta subanta (a noun form derived by adding a krt suffix)
    - level 3: vyutpanna taddhita subantas
    - level 4: vyutpanna uttarapadas of a compound

### tinanta generator
Regarding the level parameter:
 - In the case of verb forms, I have only level 1. The Nijantas, though derived are assigned the same level.
