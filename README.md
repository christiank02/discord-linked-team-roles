# Discord Role Connection Bot

This project is a Spring Boot application that allows users to connect their Discord roles to the bot and match their rank in Discord to an equivalent connection role.

## Features

- Register metadata for role connections in Discord.
- Connect to the bot to match your rank in Discord to an equivalent connection role.

## Prerequisites

- Java 11 or higher
- Maven
- A Discord bot token
- Microsoft SQL Server

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/yourusername/discord-role-connection-bot.git
cd discord-role-connection-bot
```

### Configuration

1. **Database Configuration**: Update the `application.properties` file with your Microsoft SQL Server details.

    ```ini
    sql.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=bot
    sql.datasource.username=sa
    sql.datasource.password=yourpassword
    ```

2. **Discord Bot Token**: Add your Discord bot token to the `application.properties` file.

    ```ini
    discord.bot.token=your_discord_bot_token
    ```

3. **Guild ID**: Add your Discord guild ID to the `application.properties` file.

    ```ini
    AIMLESS_BOT_GUILD_ID=your_guild_id
    ```

4. **OAuth Provider Configuration**: Add the following OAuth provider details to the `application.properties` file.

    ```ini
    oauth.provider.token.url=https://discord.com/api/oauth2/token
    oauth.provider.authorization.url=https://discord.com/oauth2/authorize
    oauth.provider.api.url=https://discord.com/api/
    oauth.provider.userinfo.url=https://discord.com/api/oauth2/@me
    oauth2.user.agent=Discord Bot
    ```

5. **OAuth Client Configuration**: Add the following OAuth client details to the `application.properties` file.

    ```ini
    bot.client.id=your_client_id
    bot.client.secret=your_client_secret
    bot.client.name=DiscordBot
    bot.client.claim.name=username
    bot.client.redirect.uri=[http://your_redirect_uri]/login/oauth2/code/discord
    bot.client.scope=role_connections.write,identify
    ```

6. **Discord Developer Portal Configuration**: Set the following settings for your Discord Application in the Discord Developer Portal.

    ```ini
    Redirects: [http://your_redirect_uri]/login/oauth2/code/discord
    Linked Roles Verification URL: [http://your_redirect_uri]/oauth2/authorization/discord
    ```
### Build and Run

```sh
mvn clean install
mvn spring-boot:run
```

## Usage

### Register Metadata

The bot provides a command to register metadata for role connections. Use the `/registermetadata` command in your Discord server to register the necessary metadata.

### Add Role

The owner/admin needs to set up roles as follows:

1. **Create a Role and a Matching Connection Role**:
    - Create a normal role (e.g., Admin Role).
    - Create a matching connection role (e.g., Admin) using the bot's connection feature and assign it a `role_level` (e.g., 5).

2. **Use the `/linkedrole add` Command**:
    - Use the command `/linkedrole add [role, the normal Role (e.g., Admin Role)] [level (e.g., 5)]` to add the role and its level.

After this setup, users with the role (e.g., Admin Role) can connect to the connection role (e.g., Admin).

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
```

Make sure to replace placeholders like `yourusername`, `yourpassword`, `your_discord_bot_token`, `your_client_id`, `your_client_secret`, and `your_guild_id` with actual values.