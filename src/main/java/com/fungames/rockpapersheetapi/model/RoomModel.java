package com.fungames.rockpapersheetapi.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@Getter
public class RoomModel {
    private static final long MAX_ACTIVITY_DELAY = 60 * 1000; // 60 sec

    private UUID id;
    private RoomUserModel user1;
    private RoomUserModel user2;
    private RoomGameModel game;
    private RoomGameModel result;
    private RoomScoreModel score;

    public RoomModel(){
        id = UUID.randomUUID();
        score = new RoomScoreModel();
        game = new RoomGameModel();
    }

    public boolean isAbandoned() {
        if(user1 != null && user2 != null) {
            if (!user1.isActive() || !user2.isActive())
                return true;
        }

        return false;
    }

    public boolean isEmptyRoom() {
        if(user1 == null && user2 == null) return true;
        return false;
    }

    public boolean isWaitingToUsers() {
        if(user1 != null && user1.isActive() && user2 == null) return true;
        return false;
    }

    public boolean isReadyToStart(){
        if(user1 != null && user1.isActive() && user2 != null && user2.isActive())
            return true;
        return false;
    }

    public void clearRoom(){
        user1 = null;
        user2 = null;
        score = new RoomScoreModel();
        game = new RoomGameModel();
        result = null;
    }

    public UUID setUserAnswer(UUID userId, GameItem item){
        UUID id = game.getId();

        if(user1.getId().equals(userId)) {
            if(game.getAnswerUser1() == null) {
                game.setAnswerUser1(item);
                checkResult();
            }
        } else if(user2.getId().equals(userId)) {
            if(game.getAnswerUser2() == null) {
                game.setAnswerUser2(item);
                checkResult();
            }
        }

        return id;
    }

    private void checkResult() {
        if(game.getAnswerUser1() != null && game.getAnswerUser2() != null) {
            if(game.getAnswerUser1() == GameItem.PAPER && game.getAnswerUser2() == GameItem.ROCK
                    || game.getAnswerUser1() == GameItem.ROCK && game.getAnswerUser2() == GameItem.SCISSORS
                    || game.getAnswerUser1() == GameItem.SCISSORS && game.getAnswerUser2() == GameItem.PAPER
            ) {
                score.setUser1(score.getUser1() + 1);
            } else if(game.getAnswerUser1() != game.getAnswerUser2()) {
                score.setUser2(score.getUser2() + 1);
            }

            result = RoomGameModel.of(game);
            game = new RoomGameModel();
        }
    }

    public boolean hadAccess(UUID userId){
        return getUser(userId).isPresent();
    }

    public boolean hasResult(){
        return result != null;
    }

    public GameItem getEnemyItem(UUID userId){
        if(user1 != null && user1.getId().equals(userId)) {
            return result.getAnswerUser2();
        } else {
            return result.getAnswerUser1();
        }
    }

    public void addUser(RoomUserModel user){
        if (user1 == null) user1 = user;
        else if (user2 == null) user2 = user;
    }

    public Optional<RoomUserModel> getUser(UUID userId){
        if (user1 != null && user1.getId().equals(userId)) {
            user1.setLastActivity(System.currentTimeMillis());
            return Optional.of(user1);

        } else if (user2 != null && user2.getId().equals(userId)) {
            user2.setLastActivity(System.currentTimeMillis());
            return Optional.of(user2);
        }
        return Optional.empty();
    }
}
