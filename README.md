[fsu-invite]: https://discord.gg/xgJH9uk
[lgtm]: https://lgtm.com/projects/g/Excaliburns/NoleBot/context:java

![GitHub release](https://img.shields.io/github/release/excaliburns/nolebot.svg)
![GitHub All Releases](https://img.shields.io/github/downloads/excaliburns/nolebot/total.svg)
[ ![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Excaliburns/NoleBot.svg?logo=lgtm&logoWidth=18) ][lgtm]
[ ![Discord](https://discordapp.com/api/guilds/138481681630887936/widget.png) ][fsu-invite]

# NoleBot
NoleBot is a Discord bot written for the Esports at Florida State Discord, written in Java using JDA.

## Functionality

* Level based permission system
  * Allows for sub-leveled role assignment with verification further than what discord allows.
* *that's it for now..*
  * Planned features include student verification, game integration specific to FSU, and other student-driven features.

If you would like custom functionality added: Please contact tut#0001 on Discord, or open an issue on GitHub.

## Setup
Setting up the bot can be done two ways:
<br><br>

### Gradle
Clone the git repo and build with gradle.

`gradle clean build`
or
`./gradlew clean build`


You will have to run the bot from the command line at least once to generate config.properties. 

Navigate to the directory with the bot jar and run the command `java -jar [botjar].jar`.

You can also make the file yourself. Navigate to the directory that contains the bot jar, and make a subdirectory called data, and another inside called config. Make the file as such:

`data/config/config.properties`
```
token=
```

<br>

### Release
Download the most recent release, at the top of the page here.
There are two files, a .jar, and config.properties.
<br><br>
### After Downloading
First, get your bot token from discord. You can follow [this](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token) guide to creating a bot user and getting a token.

Navigate to where the bot jar is and open `data/config/config.properties`. In the line that shows `token=`, paste your token as such.

`token=YOURTOKENHERE`

After inputting your token, you must then run the bot from the command line.
Open the same directory as the bot is in and run the command `java -jar [botjar].jar`

Congrats! Your bot is now running. See the Wiki for more information about what to do now.
## Wiki
Information and configuration settings for NoleBot can be found on the wiki [here](https://github.com/Excaliburns/NoleBot/wiki).

## Dependencies
NoleBot requires at least **Java 8** to run.
