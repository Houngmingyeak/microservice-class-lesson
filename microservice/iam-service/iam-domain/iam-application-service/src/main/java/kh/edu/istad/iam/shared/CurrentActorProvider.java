package kh.edu.istad.iam.shared;

/**
 * Output port: who is performing the current operation (JWT sub or "system").
 * Implemented by iam-infrastructure.
 */
public interface CurrentActorProvider {

    String currentActor();
}