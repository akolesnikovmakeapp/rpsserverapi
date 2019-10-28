package com.fungames.rockpapersheetapi.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RoomModel {
    private static final long MAX_ACTIVITY_DELAY = 60 * 1000; // 60 sec

    private UUID id;
    private RoomUserModel user1;
    private RoomUserModel user2;
    private RoomAnswerModel answer;
    private RoomAnswerModel result;
    private RoomScoreModel score;

    public RoomModel(){
        id = UUID.randomUUID();
        score = new RoomScoreModel();
        answer = new RoomAnswerModel();
    }

    public boolean isAbandoned() {
        if(user1 != null && user2 != null) {
            if (!user1.isActive() || !user2.isActive()) return true;
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
        answer = new RoomAnswerModel();
    }

    public void setUserAnswer(UUID userId, GameItem item){
        if(user1.getId().equals(userId)) {
            if(answer.getAnswerUser1() == null) {
                answer.setAnswerUser1(item);
                checkResult();
            }
        } else if(user2.getId().equals(userId)) {
            if(answer.getAnswerUser2() == null) {
                answer.setAnswerUser2(item);
                checkResult();
            }
        }
    }

    private void checkResult() {
        if(answer.getAnswerUser1() != null && answer.getAnswerUser2() != null) {
            if(answer.getAnswerUser1() == GameItem.PAPER && answer.getAnswerUser2() == GameItem.ROCK
                    || answer.getAnswerUser1() == GameItem.ROCK && answer.getAnswerUser2() == GameItem.SCISSORS
                    || answer.getAnswerUser1() == GameItem.SCISSORS && answer.getAnswerUser2() == GameItem.PAPER
            ) {
                score.setUser1(score.getUser1() + 1);
            } else if(answer.getAnswerUser1() != answer.getAnswerUser2()) {
                score.setUser2(score.getUser2() + 1);
            }
            answer = new RoomAnswerModel();
            result = RoomAnswerModel.of(answer);
        }
    }

    public boolean hadAccess(UUID userId){
        if (user1 != null && user1.getId().equals(userId)) return true;
        if (user2 != null && user2.getId().equals(userId)) return true;
        return false;
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
}
