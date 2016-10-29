### About the TreeTagger directory

This directory contains the TreeTagger executable module which is performs the part-of-speech tagging of the text.  
The TreeTagger can be downloaded to the following address: 
[TreeTagger v.3.2](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/).  
For correct work, contents of this directory have to look as follows:
```code
\---TreeTagger
    |   README.md
    |
    +---bin
    |       tree-tagger.exe
    |
    \---lib
            russian-big-utf8.par
            russian-utf8.par
```
Where russian-utf8.par a simple trained tagset which can be downloaded 
[here](http://corpus.leeds.ac.uk/mocky/russian.par.gz).  
And russian-big-utf8.par the expanded trained tagset available to this 
[address](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/data/russian-par-linux-3.2-utf8.bin.gz).  
The author of both tagsets is [Serge Sharoff](http://corpus.leeds.ac.uk/mocky/).
