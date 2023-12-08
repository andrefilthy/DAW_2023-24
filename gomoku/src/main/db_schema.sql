create schema dbo;
drop table if exists dbo.Game;
drop table if exists dbo.Token;
drop table if exists dbo.User;

CREATE TABLE dbo.User (
    userAlias VARCHAR(64),
    username VARCHAR(64) unique not null,
    pwd VARCHAR(64) not null,
    numberOfGames int default 0,
    numberOfWins int default 0
);

CREATE TABLE dbo.Game (
    gameID UUID not null,
    ruleSet varchar(200),
    created int,
    currentPhase varchar(30) default 'Placing',
    currentState varchar(30),
    player1 VARCHAR(64) references dbo.User(username),
    player2 VARCHAR(64) references dbo.User(username),
    board varchar(512),
    player1_logic varchar(10),
    player2_logic varchar(10),
    turnStartedAt int
);

create table dbo.Token(
    token_validation VARCHAR(256) primary key,
    username varchar(64) references dbo.User(username) ON DELETE CASCADE,
    created_at int,
    last_used_at int,
    expires_on int
);

create table dbo.waitingList(
  player varchar(64) references dbo.User(username),
  ruleSet varchar(200)
);


