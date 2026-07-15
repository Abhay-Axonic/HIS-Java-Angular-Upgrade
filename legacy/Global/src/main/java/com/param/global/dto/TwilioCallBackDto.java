package com.param.global.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class TwilioCallBackDto {
	@JsonProperty("AccountSid")
	private String accountSid;	//All	The AccountSid associated with this Room
	@JsonProperty("RoomName")
	private String roomName;	//All	The UniqueName of the Room generating this event.
	@JsonProperty("RoomSid")
	private String roomSid;	//All	The Sid of the Room generating this event.
	@JsonProperty("RoomStatus")
	private String roomStatus;	//All	The Status of the Room generating this event.
	@JsonProperty("RoomType")
	private String roomType;	//All	The Type of the Room generating this event.
	@JsonProperty("StatusCallbackEvent")
	private String statusCallbackEvent; //	All	The Room event. For example, room-created. See Rooms Status Callback Events for the complete list.
	@JsonProperty("Timestamp")
	private String timestamp;	//All	Time of the event, conformant to UTC ISO 8601 Timestamp.
	@JsonProperty("ParticipantSid")
	private String participantSid;	//All Participant and Track Events	The Sid for the Participant generating this event.
	@JsonProperty("ParticipantStatus")
	private String participantStatus;	//All Participant and Track Events	The current Status of the Participant generating this event - either connected or disconnected.
	@JsonProperty("ParticipantDuration")
	private String participantDuration;	//Only on participant-disconnected event	The total duration the Participant remained connected to the Room.
	@JsonProperty("ParticipantIdentity")
	private String participantIdentity;	//All Participant and Track Events	The Identity of the Participant generating this event. Participant identities are set via the Participant's Access Token
	@JsonProperty("RoomDuration")
	private String roomDuration;	//Only on room-ended	The total duration of the Room, in seconds.
	@JsonProperty("SequenceNumber")
	private String sequenceNumber;	//All	An incrementing integer representing the order of the events in the Room.
	@JsonProperty("TrackSid")
	private String trackSid;	//All Track events	The Sid of the Track.
	@JsonProperty("TrackKind")
	private String trackKind;//All Track events	The Kind of the Track (data, audio or video).
	public String getAccountSid() {
		return accountSid;
	}
	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomSid() {
		return roomSid;
	}
	public void setRoomSid(String roomSid) {
		this.roomSid = roomSid;
	}
	public String getRoomStatus() {
		return roomStatus;
	}
	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getStatusCallbackEvent() {
		return statusCallbackEvent;
	}
	public void setStatusCallbackEvent(String statusCallbackEvent) {
		this.statusCallbackEvent = statusCallbackEvent;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getParticipantSid() {
		return participantSid;
	}
	public void setParticipantSid(String participantSid) {
		this.participantSid = participantSid;
	}
	public String getParticipantStatus() {
		return participantStatus;
	}
	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}
	public String getParticipantDuration() {
		return participantDuration;
	}
	public void setParticipantDuration(String participantDuration) {
		this.participantDuration = participantDuration;
	}
	public String getParticipantIdentity() {
		return participantIdentity;
	}
	public void setParticipantIdentity(String participantIdentity) {
		this.participantIdentity = participantIdentity;
	}
	public String getRoomDuration() {
		return roomDuration;
	}
	public void setRoomDuration(String roomDuration) {
		this.roomDuration = roomDuration;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getTrackSid() {
		return trackSid;
	}
	public void setTrackSid(String trackSid) {
		this.trackSid = trackSid;
	}
	public String getTrackKind() {
		return trackKind;
	}
	public void setTrackKind(String trackKind) {
		this.trackKind = trackKind;
	}
	@Override
	public String toString() {
		return "TwilioCallBackDto [accountSid=" + accountSid + ", roomName=" + roomName + ", roomSid=" + roomSid
				+ ", roomStatus=" + roomStatus + ", roomType=" + roomType + ", statusCallbackEvent="
				+ statusCallbackEvent + ", timestamp=" + timestamp + ", participantSid=" + participantSid
				+ ", participantStatus=" + participantStatus + ", participantDuration=" + participantDuration
				+ ", participantIdentity=" + participantIdentity + ", roomDuration=" + roomDuration
				+ ", sequenceNumber=" + sequenceNumber + ", trackSid=" + trackSid + ", trackKind=" + trackKind + "]";
	}
	
 
}
