use nolebot;
create table if not exists Attendance
(
    dateTime  datetime     not null,
    userID    varchar(45)  not null,
    nickname  varchar(255) not null,
    discordID varchar(255) not null,
    primary key (dateTime, userID)
);