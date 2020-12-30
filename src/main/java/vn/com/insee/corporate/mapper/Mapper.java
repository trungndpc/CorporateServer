package vn.com.insee.corporate.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.insee.corporate.mapper.dto2entity.BillDTOToBillEntity;
import vn.com.insee.corporate.mapper.dto2entity.ConstructionFormToConstructionEntity;
import vn.com.insee.corporate.mapper.dto2entity.CustomerFormToCustomerEntity;
import vn.com.insee.corporate.mapper.dto2entity.PostFormToPostEntity;
import vn.com.insee.corporate.mapper.entity2dto.CustomerEntityToCustomerDTO;
import vn.com.insee.corporate.mapper.entity2dto.PostEntityToPostDTO;
import vn.com.insee.corporate.mapper.entity2dto.UserEntityToUserDTO;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    private ModelMapper mapper;

    public Mapper() {

        this.mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.mapper.addMappings(new CustomerFormToCustomerEntity());
        this.mapper.addMappings(new PostFormToPostEntity());
        this.mapper.addMappings(new PostEntityToPostDTO());
        this.mapper.addMappings(new UserEntityToUserDTO());
        this.mapper.addMappings(new BillDTOToBillEntity());
        this.mapper.addMappings(new ConstructionFormToConstructionEntity());
        this.mapper.addMappings(new CustomerEntityToCustomerDTO());
    }
    public <S, D> void map(S source, D destination) {
        mapper.map(source, destination);
    }

    public <S, D> List<D> mapToList(List<S> list, Type type) {
        return mapper.map(list, type);
    }

    public <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> this.mapper.map(element, targetClass))
                .collect(Collectors.toList());
    }


}
