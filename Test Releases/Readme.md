Initial test release **16/12/2016 02:25:27** GPv1.jar

----------

>To use : 
> - Create new folder (ie nf), place the jar and the .osm file in the newly created folder<br>
> - Download any OSM file of your choice with any size from: http://download.geofabrik.de/, then copy the downloaded .osm to the new folder
> - Open cmd in the newly created folder path and type : java -jar GPvx.jar test-osm.osm , where x is to be replaced with what version you want to test and test-osm.osm is the file you want to parse and get data out of it

Incremental release **01/02/2017 02:24:13** GPv2.jar

----------

>1) Improved Adjacency list building time performance from 5.30 hours to 93 seconds
>2) Improve small graph performance to 331 seconds from past 1875 one
