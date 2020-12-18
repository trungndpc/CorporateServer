package vn.com.insee.corporate.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import vn.com.insee.corporate.mapper.dto2entity.CustomerFormToCustomerEntity;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class Mapper {
    private ModelMapper mapper;

    public Mapper() {
        this.mapper = new ModelMapper();
        this.mapper.addMappings(new CustomerFormToCustomerEntity());
    }
    public <S, D> void map(S source, D destination) {
        mapper.map(source, destination);
    }

    public <S, D> List<D> mapToList(List<S> list, Type type) {
        return mapper.map(list, type);
    }

}
