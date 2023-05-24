███████╗██╗  ██╗   ██╗████████╗██████╗  █████╗ ███████╗██╗  ██╗   ██╗
██╔════╝██║  ╚██╗ ██╔╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██║  ╚██╗ ██╔╝
█████╗  ██║   ╚████╔╝    ██║   ██████╔╝███████║█████╗  ██║   ╚████╔╝
██╔══╝  ██║    ╚██╔╝     ██║   ██╔══██╗██╔══██║██╔══╝  ██║    ╚██╔╝
███████╗███████╗██║      ██║   ██║  ██║██║  ██║██║     ███████╗██║
╚══════╝╚══════╝╚═╝      ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝╚═╝ by Max - v1.0.0

+--------------+
| Dependencies |
+--------------+

- Paper 1.19.2

- Eine Internetverbindung - Das Plugin lädt die benötigten Dependecies dynamisch nach und checkt nach updates

+--------------+
| Installation |
+--------------+

-> Plugin in den Plugins Ordner kopieren.

+---------+
| Updates |
+---------+

-> Das Plugin checkt automatisch beim starten ob ein Update verfügbar ist.
-> Falls ja wird dies in der Konsole beim starten angezeigt.

+---------------+
| Konfiguration |
+---------------+

-> Config
 |-> Prefix    : Prefix des Plugins - MiniMessage encoded
 |-> LogLevel  : Log Level des Plugins (Legt fest welche Nachrichten in der Konsole angezeigt werden - einfach lassen).
 |-> locations : Locations des Bereiches in dem Spieler die Elytra erhalten sollen. (LASSEN!)

-> Command
 |-> /elytrafly <1|2> - Damit zwei gegenüberliegende Ecken des bereiches in dem Spieler die Elytra erhalten sollen markieren.

+------------+
| Funktionen |
+------------+

-> Wenn Spieler sich in dem markierten Bereich befinden erhalten sie eine Elytra.

-> Das Plugin speichert die Chestplate des Spielers und gibt sie ihm wieder wenn die Elytra entfernt wird.

-> Sobald der Spieler landet wird die Elytra entfernt und die Chestplate wieder angezogen.