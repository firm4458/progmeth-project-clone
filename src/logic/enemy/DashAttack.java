package logic.enemy;

import application.GameManager;

public class DashAttack extends AttackScriptFactory {
	private double finalY;

	public DashAttack(double finalY) {
		this.finalY = finalY;
	}

	@Override
	public AttackScript createScript() {
		return new DashAttackScript(finalY);
	}

	public class DashAttackScript extends AttackScript {
		private double originalX;
		private double finalY; // final destination in y-axis
		private long start;
		private int count = 0;
		private boolean looped = false;

		public DashAttackScript(double finalY) {
			this.finalY = finalY;
		}

		@Override
		public void onAttach() {
			originalX = parent.getX();
			System.out.println(finalY);
		}

		/**
		 * update parent by translate it to final destination	
		 */
		@Override
		public void update() {
			if (looped) {
				parent.translate(0, 20);
				if (parent.getY() >= finalY) {
					parent.setY(finalY);
					setDone(true);
				}
				return;
			}

			if (parent.getY() < GameManager.NATIVE_HEIGHT) {
				parent.translate(0, 1 * ((count++) / 3.0));
				start = System.currentTimeMillis();
			} else if (System.currentTimeMillis() - start > 500) {
				parent.setX(originalX);
				parent.setY(-parent.getSprite().getHeight());
				looped = true;
			}
		}
	}

}
