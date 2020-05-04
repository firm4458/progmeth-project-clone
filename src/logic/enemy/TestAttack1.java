package logic.enemy;

import logic.base.GameInterruptException;

public class TestAttack1 extends AttackScriptFactory {
	
	private long duration;
	private String msg;
	
	public TestAttack1(long duration, String msg) {
		this.setDuration(duration);
		this.setMsg(msg);
	}

	@Override
	public AttackScript createScript() {
		return new TestAttack1Script(duration,msg);
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public class TestAttack1Script extends AttackScriptFactory.AttackScript {
		private long now;
		private long duration;
		private String msg;
		private long start;
		
		private TestAttack1Script(long duration, String msg) {
			start = System.currentTimeMillis();
			this.duration = duration;
			this.msg = msg;
		}

		@Override
		public void update() throws GameInterruptException {
			now = System.currentTimeMillis();
			if(now-start<=duration) {
				System.out.println(msg);
			} else {
				setDone(true);
			}
						
		}
		
	}

}
