scaldingbot
===========

Scala based MediaWiki bot

This bot has nothing to do with https://github.com/twitter/scalding , which came up with the same name I did a year earlier.

# Documentation

None yet. Write some!

# Build status

The build may currently be spuriously failing. Issues in failing are that 1. No password is set in the unit tests, failing login, and 2. because tests are run in parralel, login can fail because a second login token may be requested while a first login cycle ins't fully completed yet, invalidating the first token. Mitigating pull requests are very welcome, as I don't know how to solve that.

[![Build Status](https://travis-ci.org/martijnhoekstra/scaldingbot.png?branch=master)](https://travis-ci.org/martijnhoekstra/scaldingbot)
