package br.com.elotech.desafio.backend.taskmanager.domain.specifications;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.TaskFilterPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> filterTasks(TaskFilterPostDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }

            if (filter.priority() != null) {
                predicates.add(cb.equal(root.get("priority"), filter.priority()));
            }

            if (filter.responsibleId() != null) {
                predicates.add(cb.equal(root.get("responsible").get("id"), filter.responsibleId()));
            }

            if (filter.responsibleId() != null) {
                predicates.add(cb.equal(root.get("project").get("id"), filter.projectId()));
            }

            if (filter.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("commonData").get("createdAt"), filter.startDate()));
            }

            if (filter.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("commonData").get("createdAt"), filter.endDate()));
            }

            if (StringUtils.hasText(filter.searchText())) {
                String pattern = "%" + filter.searchText().toLowerCase() + "%";
                Predicate titlePredicate = cb.like(cb.lower(root.get("title")), pattern);
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), pattern);

                predicates.add(cb.or(titlePredicate, descriptionPredicate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
