Endpoints requests: (port=8080)

Requests for suspicious activities:
http://localhost:8080/show_suspicious_activity_1a/{startPeriodTime}/{endPeriodTime}
http://localhost:8080/show_suspicious_activity_1b
http://localhost:8080/show_suspicious_activity_1c

Requests for specific game statistics: (for bet,win and profit)
http://localhost:8080/show_game_statistics/bet/{gameName}/{startPeriodTime}/{endPeriodTime}
http://localhost:8080/show_game_statistics/win/{gameName}/{startPeriodTime}/{endPeriodTime}
http://localhost:8080/show_game_statistics/profit/{gameName}/{startPeriodTime}/{endPeriodTime}

Requests for all games statistics: (for bet,win and profit)
http://localhost:8080/show_all_game_statistics/bet/{startPeriodTime}/{endPeriodTime}
http://localhost:8080/show_all_game_statistics/win/{startPeriodTime}/{endPeriodTime}
http://localhost:8080/show_all_game_statistics/profit/{startPeriodTime}/{endPeriodTime}