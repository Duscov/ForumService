package cohort_65.java.forumservice.accounting.dao;

import cohort_65.java.forumservice.accounting.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
}