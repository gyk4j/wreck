[![Java CI with Maven](https://github.com/gyk4j/wreck/actions/workflows/maven.yml/badge.svg)](https://github.com/gyk4j/wreck/actions/workflows/maven.yml)

# About WRECK

WRECK is a Java desktop utility tool to *restore the file system timestamp* 
*attributes of files using the recorded metadata* if possible and rules / 
heuristics as a backup method on a *best-effort basis*. (See 
[Caveats](../../wiki/Caveats) for more details on the assumptions made).

The name WRECK is an acronym for **Walk, Retrieve, Extract, Correct and Keep**.

* Walk a given starting path or a single file
* Retrieve each file or directory encountered
* Extract metadata from it
* Correct the file system timestamps (creation, last modified, last accessed)
  using embedded metadata. In the absence of any useful metadata, it fallbacks 
  to using rules, heuristics and guesswork for estimating an approximate 
  appropriate timestamp on a best-effort basis (Refer to [Caveats](../../wiki/Caveats)).
* Keep the files for archival and preservation

It essentially processes files and directories recursively, extracting embedded 
metadata from common file formats (e.g. Microsoft Office documents, JPEG 
photos, MP4 videos, MP3 audio) using a few libraries to maximize success in 
metadata extraction, work out the appropriate timestamps, correct them in the
file system, and finally leaving the files ready for archival and preservation.

![Timestamps Fixed](../../wiki/assets/images/settings-fixed.png)
![WRECK Running](../../wiki/assets/images/settings-running.png)

For more information, see the [Wiki](../../wiki/About-WRECK).
