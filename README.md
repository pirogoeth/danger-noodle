danger-noodle
=============

(aka Havabol)


Status
======

Some base code is laid out. We can easily swap out the current `Scanner` code
(by Sean) with another once the decision is made.

No interpreting actually happens at this point. Just scanning, token construction,
and classification.


Building
========

On a Linux machine (or in Cygwin / MinGW under Windows) run `ci/build.sh` from the root folder.
The script will drop a `danger-noodle.jar` in the root folder. Done!


Testing
=======

Plans for continuous integration testing are still in progress.
Looking in to using [Concourse CI](https://concourse.ci) as the testing platform.
Pipeline and build steps will be placed under the `ci` folder - see `ci/pipeline.yml` and the other `.yml`
files.


Contributing
============

Please attempt *not* to commit directly to master. It makes code review
considerably easier when all work is done from a separate branch and then PR / reviewed
into master.

Sample flow:

```
git checkout -b my_feature_branch
# ... do work
git add .
git commit -m "I did some things!"
git push --set-upstream origin my_feature_branch
```

Back on Github, create a PR against the base branch with your feature branch.
Post to Slack and then profit. After changes are approved (with Github's review features)
and at least one member approves (and tests pass!), you can squash merge into master.
