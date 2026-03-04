## 🎮 Online Ending Screen System Documentation

### Overview
The online ending screen system provides multiplayer game conclusion with scoring, ranking, and competitive winner determination.

---

### Components

#### 1. **GameResult.java** - Score Calculation & Ranking
- **Purpose**: Tracks individual player scores and determines winners
- **Scoring Formula**: `Final Score = Affection + Stamina`
- **Winner Determination**: Highest combined score wins
- **Network Format**: Serializable as CSV string for transmission

**Key Methods**:
```java
GameResult.determineWinner(results)      // Get 1st place player
GameResult.rankPlayers(results)          // Rank all players 1-3
```

---

#### 2. **OnlineEndingScreen.java** - Visual Result Display
- **Purpose**: Shows win/lose screen with rankings
- **Features**:
  - 🏆 Winner announcement (Gold header)
  - 💔 Loser indication (Red header)
  - 📊 Player stats card (Name, Character, Affection, Stamina, Score)
  - 🏅 Leaderboard with medals (🥇 🥈 🥉)

**Display Sections**:
1. Header: Win/Lose status with emoji
2. Your Stats: Personal game results
3. Rankings: All players sorted by score

---

#### 3. **QuestionTimer.java** - 30-Second Question Limit
- **Purpose**: Enforce 30-second time limit per choice question
- **Features**:
  - Countdown from 30 to 0 seconds
  - Per-second callbacks for UI updates
  - Critical state detection (<5 seconds)
  - Percentage calculation for progress bars

**Usage Example**:
```java
QuestionTimer timer = new QuestionTimer();
timer.setOnTimeUp(() -> {
    System.out.println("Time's up! Moving to next question.");
});
timer.setOnTick(remaining -> {
    updateTimerDisplay(remaining); // Update UI every second
});
timer.start();
```

---

#### 4. **GameServer.java** - Multiplayer Game Coordinator
- **New Features**:
  - `finishedPlayers` map: Tracks completed players
  - `gameEnded` flag: Signals when all players finished
  - `FINISH_GAME:` protocol: Receives player results

**New Message Formats**:
```
CLIENT → SERVER: FINISH_GAME:affection:stamina:character
SERVER → ALL:    GAME_ENDED:p1_result;p2_result;p3_result;
```

**Game Flow**:
1. All players start at same time
2. Each player plays individually with 30-sec timers
3. When a player finishes, they send FINISH_GAME
4. When last player finishes, server broadcasts GAME_ENDED
5. All clients show OnlineEndingScreen simultaneously

---

#### 5. **GameClient.java** - Network Communication
- **New Features**:
  - `gameResults` map: Stores final scores
  - `finishGame()`: Send completion to server
  - `setOnGameEndListener()`: Listen for results

**Methods Added**:
```java
client.finishGame(affection, stamina, character);  // Send results
client.getGameResults();     // Get all results
client.getMyResult();        // Get personal result
client.setOnGameEndListener(results -> {
    // Show ending screen when server broadcasts
});
```

---

### Game Winner Determination

**Scoring System**:
```
Player 1: Affection=50 + Stamina=40 = 90 points
Player 2: Affection=45 + Stamina=30 = 75 points
Player 3: Affection=35 + Stamina=25 = 60 points

Winner: Player 1 (90 points) 🏆
```

**Tie-Breaker**: If scores equal, player who finished first wins (by timestamp).

---

### Integration Steps

#### Step 1: Add Timer to GameGui Choice Buttons
```java
// In addChoice() method when creating choice button
QuestionTimer timer = new QuestionTimer();
timer.setOnTimeUp(() -> {
    // Auto-select random choice or penalty
    JOptionPane.showMessageDialog(this, "Time's up!");
});
timer.setOnTick(remaining -> {
    timerLabel.setText("⏰ " + remaining + "s");
    if (timer.isCritical()) {
        timerLabel.setForeground(Color.RED);
    }
});
timer.start();
```

#### Step 2: Call finishGame() When Ending
```java
// When showEnding() is called in GameGui
int affection = player.getAffection();
int stamina = staminaManager.getStamina();
gameClient.finishGame(affection, stamina, characterKey);
```

#### Step 3: Listen for Game End
```java
gameClient.setOnGameEndListener(results -> {
    GameResult myResult = results.get(gameClient.getMyId());
    List<GameResult> allResults = new ArrayList<>(results.values());
    new OnlineEndingScreen(myResult, allResults);
});
```

---

### Message Protocol

**TCP Messages** (Port 12345):

| Direction | Message | Example |
|-----------|---------|---------|
| C→S | JOIN | `JOIN:Alice` |
| C→S | SELECT | `SELECT:Maprang` |
| C→S | UPDATE | `UPDATE:3:45` |
| C→S | FINISH_GAME | `FINISH_GAME:50:40:Maprang` |
| S→C | SYNC | `SYNC:1,Alice,Maprang,3,45;` |
| S→C | GAME_ENDED | `GAME_ENDED:1,Alice,Maprang,50,40,90,...;` |

---

### Waiting Behavior

**Player A finishes at 5:30 PM**
- Sees loading screen: "⏳ รอผู้เล่นคนอื่น..."

**Player B finishes at 5:35 PM**
- A still waiting

**Player C finishes at 5:40 PM**
- Server broadcasts GAME_ENDED
- All three see OnlineEndingScreen simultaneously

---

### UI Features

#### Winner Screen 🏆
- Gold/Yellow header
- "🏆 คุณชนะ! 🏆" title
- Statistics highlighted
- Gold medal in rankings

#### Loser Screen 💔
- Red header
- "💔 คุณแพ้ 💔" title
- Statistics in normal colors
- Silver/Bronze medal in rankings

#### Leaderboard
- Rank badges: 🥇 🥈 🥉 ⭐
- Player names + characters
- Three metrics: 💖 Affection | ⚡ Stamina | 🎯 Score
- Color-coded by rank

---

### Files Modified/Created

| File | Type | Changes |
|------|------|---------|
| `GameResult.java` | NEW | Score tracking & ranking |
| `OnlineEndingScreen.java` | NEW | Visual end screen |
| `QuestionTimer.java` | NEW | 30-sec question timer |
| `GameServer.java` | MODIFY | Add game completion logic |
| `GameClient.java` | MODIFY | Add result handling |

---

### Testing Checklist

- [ ] 3 clients connect to server
- [ ] Start game button available with 3 players
- [ ] Timers count down on each player's screen
- [ ] Player 1 finishes → shows waiting screen
- [ ] Player 2 finishes → still waiting
- [ ] Player 3 finishes → all see ending screen
- [ ] Winner highlighted with 🏆
- [ ] Losers show 💔
- [ ] Rankings correct (highest score first)
- [ ] Medals correct (🥇🥈🥉)
