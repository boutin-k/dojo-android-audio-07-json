package fr.wildcodeschool.dojo_android_obb.obb;

public interface ObbManagerListener {
  public void onObbStateChange(String path, int state);
}
