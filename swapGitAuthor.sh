#!/bin/sh
 
git filter-branch --env-filter '
 
an="$GIT_AUTHOR_NAME"
am="$GIT_AUTHOR_EMAIL"
cn="$GIT_COMMITTER_NAME"
cm="$GIT_COMMITTER_EMAIL"
 
if [ "$GIT_COMMITTER_EMAIL" = "timp@paneris.org" ]
then
    cn="Tim Pizey"
    cm="timp21337@paneris.org"
fi
if [ "$GIT_AUTHOR_EMAIL" = "timp@paneris.org" ]
then
    an="Tim Pizey"
    am="timp21337@paneris.org"
fi
 
export GIT_AUTHOR_NAME="$an"
export GIT_AUTHOR_EMAIL="$am"
export GIT_COMMITTER_NAME="$cn"
export GIT_COMMITTER_EMAIL="$cm"
'
