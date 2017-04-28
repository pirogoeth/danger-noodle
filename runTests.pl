#!/usr/bin/perl
@testFiles = `find p5TestFiles/ -name "*.txt"`;
chop @testFiles;
$i = 0;
for $testFile (@testFiles)
{
    print "______________________________________________________________________________________________________\n";
    print "|                                                                                                    |\n";
    print " This is the current test file: ".$testFile."\n\n";
    $results = `java -cp danger-noodle.jar havabol.HavaBol $testFile`;
    print $results;
    print "|____________________________________________________________________________________________________|\n\n";
    $i++;
}
print $i;
#print @testFiles;
