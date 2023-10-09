package ra.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.Catalog;

@Repository
public interface ICatalogRepository extends JpaRepository<Catalog,Long> {
}
