---
title: Data Model
description: 'UML class diagram, entity-relationship diagram, and DDL.'
order: 20
---

{% include ddc-abbreviations.md %}

## Page contents
{:.no_toc:}

- ToC
  {:toc}

## UML class diagram
[![DeltaDraft UML Class Diagram](img/delta-draft-uml.svg)](pdf/delta-draft-uml.pdf)

## Entity Relationship Diagram
[![DeltaDraft UML Class Diagram](img/delta-draft-erd.svg)](pdf/delta-draft-erd.pdf)

## Data Definition Language code

{% include linked-file.md file="sql/ddl-server.sql" type="sql" %}

## Implementation

## Entity Classes

- ['User'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/model/entity/User.java)
- ['Player'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/model/entity/Team.java)
- ['Team'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/model/entity/Team.java)
- ['TeamWithPlayers'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/model/entity/crossref/playerteam/TeamWithPlayers.java)
- ['PlayerWithUser'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/model/entity/crossref/playeruser/PlayerWithUsers.java)

## Repository Classes

- ['Player Repository'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/service/PlayerRepository.java)
- ['User Repository'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/service/UserRepository.java)

## Service Classes

- ['DatabaseReader'](https://github.com/ddc-java-21/delta-draft-tmomonet/blob/main/app/src/main/java/edu/cnm/deepdive/deltadraft/service/csv/DatabaseReader.java)