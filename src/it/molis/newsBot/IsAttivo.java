package it.molis.newsBot;

public class IsAttivo {

	private boolean isAttivo;
	private long chat_id;

	public IsAttivo(long chat_id) {
		this.chat_id = chat_id;
	}

	public boolean isAttivo() {
		return isAttivo;
	}

	public void setAttivo(boolean isAttivo) {
		this.isAttivo = isAttivo;
	}

	public long getChat_id() {
		return chat_id;
	}

	public void setChat_id(long chat_id) {
		this.chat_id = chat_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (chat_id ^ (chat_id >>> 32));
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
		IsAttivo other = (IsAttivo) obj;
		if (chat_id != other.chat_id)
			return false;
		return true;
	}

}
