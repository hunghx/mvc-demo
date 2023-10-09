package ra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.dao.ICatalogRepository;
import ra.model.Catalog;
import ra.service.ICatalogService;

import java.util.List;

@Service
public class CatalogService implements ICatalogService {
    @Autowired
    private ICatalogRepository catalogDao;
    @Override
    public List<Catalog> getAll() {
        return catalogDao.findAll();
    }

    @Override
    public Catalog findById(Long id) {
        return catalogDao.findById(id).orElse(null);
    }

    @Override
    public void save(Catalog catalog) {
            catalogDao.save(catalog);
    }

    @Override
    public void deleteById(Long id) {
    catalogDao.deleteById(id);
    }
}
