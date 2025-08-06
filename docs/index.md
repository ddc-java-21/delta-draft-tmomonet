---
title: Overview
description: "Project proposal or summary of in-progress/completed project."
order: 0
---

{% include ddc-abbreviations.md %}

## Page contents

{:.no_toc}

- TOC
{:toc}

## Summary

Delta Draft is a new exciting kit in the arsenal of fantasy baseball aficionados, Major League
bloggers, casual fans, and sabermetricians alike. Leveraging the power of baseball-reference.com, you can
access stats faster than an Aroldis Chapman four-seam fastball.

### Intuitive UI

You can use the app to ask all the right questions, sort players based on criteria of interest, and
for the fantasy baseball fiends, filter players that may or may not fit your draft strategy.
Incorporating
traditional metrics such as on-base percentage as well as advanced statistics such as hard hit rate
and exit velocity,
you can target your next draft breakout. Save players you find interesting. Put together teams of
your draft darlings so
you never find yourself scrambling when you're on the clock.

### What is Delta

The crown jewel of the statistical diamond is Delta, the difference between batting average on balls
in play,
"BABIP", and batting average. High variance between the two can point to a player experiencing bad
luck
and hard outs, primed to succeed to the surprise of your league mates, or a player that seemed to be
raking the past season, but underlying metrics point to a hard regression.

## Intended users and user stories

* Fantasy Fanatics

> As a member of several workplace fantasy leagues, I want to make sure that my draft goes off without
> a hitch so I can finally end Jeff in accounting's reign of terror and take home the title.

* Casual Fans

> As a transplant to Cincinnati, I've been looking to meet new people and find new hobbies. I decided
> on baseball, and I want to be informed so that I can talk ball with my new friends.

* Bloggers

> I write for a blog about my local, small market MLB team and during Hot Stove season, I want to
> have accurate, statistically backed opinions on new signings or trade acquisitions.

## Functionality

* Search and Browse MLB players with access to comprehensive statistics from baseball-reference.com
* Sort players by various performance metrics (e.g., OBP, HR, AVG, Exit Velocity, etc.)
* Save players for quick access or tracking
* Highlight players with notable delta values to identify potential breakout or regression candidates
* Compare and contrast stats across seasons to monitor improvement or decline
* Offline access to previously loaded data
* Google sign-in for security and no need to create an account
* Responsive, easy to use UI

## Persistent data

* User
    * Display name
    * OAuth2.0 identifier
    * Timestamp of first login to the app
    * List of Teams/Favorited Players

* Player
    * Player Name
    * baseball-reference URL
    * Standard/Advanced Statistics

* Team
    * Display Name
    * Lists of Players
    * Timestamp of Team creation and update

## Device/external services

* Google Sign-in
* Oauth2 Authentication
* [Baseball Reference](https://www.baseball-reference.com/)

## State of Completion

* Adding and handling of player thumbnails, both in the search view list, as well as in the individual player pages
* Implementation of a list view and a somewhat more visually appealing "baseball diamond"-like 
team display with logic to handle dragging and dropping players, while disallowing invalid placements
based on positional ineligibility.
* More extensive statistics in player view and search to allow users more modularity and information when 
looking up players. 
* Sorting buttons on statistical category columns
* Search bar for players by name
* Custom queries and filter logic in search.

## Stretch goals and possible enhancements

* Yahoo Fantasy API access for average draft position and ranking based on league-specific stat values,
or accounting for different scoring systems.
* Baseball savant scraping for additional statistics such as sprint speed, barrel rate, (possible spray chart
implementation), hitter performance against certain pitch types
* Pitcher stats and analysis
* Suggested queries and explanations for conclusions that can be reached from combinations of stats
