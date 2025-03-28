# Run RabbitMQ in Docker
- docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management

# Refresh Config at Runtime with SpringCloud Bus and SpringClod Config Monitor
- Add dependency org.springframework.cloud:spring-cloud-config-monitor
  - This adds a path /monitor which calls /busrefresh when invoked
- Add destination from hookdeck (https://console.hookdeck.com/)
  - This allows to have a public URL to be invoked when github configuration is updated (localhost will not work)
  - In Windows:
    - Install Scoop
    - Add Hookdeck: scoop bucket add hookdeck https://github.com/hookdeck/scoop-hookdeck-cli.git
    - Install Hookdeck: scoop install hookdeck
    - Login from Powershell: hookdeck login --cli-key 3pz7bk0zva0arvm5mviwivumiy6ketbha1cmxg9dh7qln2qmii
    - Listen in /monitor with Hookdeck: hookdeck listen 8071 Source --path /monitor
- Add Github Webhook: https://github.com/jmodevel/springcloud-config/settings/hooks/new
  - Invokes public URL when repo is updated
  - Add public URL from Hookdeck, type: application/json and only trigger when push in repo is done
