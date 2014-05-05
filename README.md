Yapa embeds all the required resources of a given external program (C, C++, Python, R, Octave, Scilab, etc) including libraries and packages it in a ready to use OpenMOLE project. 

Let us first mention that Yapa runs on and for Linux achitectures (that is why it partially answers the issue).  It is due to the fact that Yapa rely on the CDEPack technology, which only supports linux architecture. Fortunately, it is the best one and most servers run Linux! MacOS and Windows users have thus 2 options to package their applications: move to a real scientific OS (based on Linux for those who have not followed) or run Yapa in a virtal machine.
 
Well, **how to run Yapa**? [(Watch the online video)][1] *(ERRATUM: the zip archive has been moved to a tgz archive containing an executable yapa file)*

 

 1. Download it
 2. Untar the archive
 3. Move to the bin directory cd /yapa-0.1/bin
 4. Run it!

    ./yapa -o <outputDirectory> -c <fullCommand>

    The options:  
    -o (compulsory): the output directory where the packaging archive and the OpenMOLE project are generated  
    -c (compulsory): the full command with parameters of you code. It must be enclosed in quotation marks  
    -e (optional)  : are the program resources embed in the OpenMOLE project ? true / false. It is recomended to render your OpenMOLE workflow fully portable. The default value is true.  
    -i (optional)  : the list of resources that have to be ignored in the final archive

 
 
Read a complete tutorial [here][2]


  [1]: http://www.openmole.org/files/yapa.mp4
  [2]: http://www.openmole.org/documentation/package-your-external-applications-with-yapa
