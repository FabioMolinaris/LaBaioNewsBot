package it.molis.baionetta.beans;

public class Chat {

	private boolean isAttivo;
	private long chatId;

	public Chat(long chat_id) {
		this.chatId = chat_id;
	}

	public boolean isAttivo() {
		return isAttivo;
	}

	public void setAttivo(boolean isAttivo) {
		this.isAttivo = isAttivo;
	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chat_id) {
		this.chatId = chat_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (chatId ^ (chatId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chat other = (Chat) obj;
		if (chatId != other.chatId)
			return false;
		return true;
	}
}
