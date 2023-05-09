import {
    Create,
    Datagrid,
    Edit,
    EditButton,
    List,
    NumberInput,
    ReferenceInput,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput,
    useGetList,
} from "react-admin";
import {useEffect, useState} from "react";
import {useFormContext} from 'react-hook-form';

export const EngineList = () => (
    <List>
        <Datagrid>
            <TextField source="item.id" label="id"/>
            <TextField source="item.slotId" label="slot"/>
            <TextField source="item.characterName" label="character"/>
            <TextField source="item.itemTypeId" label="item type"/>
            <TextField source="item.upgradeLevel" label="level"/>
            <TextField source="item.nameRu" label="name"/>
            <TextField source="item.dscRu" label="dsc"/>
            <TextField source="item.cost" label="cost"/>
            <TextField source="speed"/>
            <TextField source="jump"/>
            <TextField source="engineTypeId"/>
            <TextField source="engineType.nameRu" label="base name"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export const EngineEdit = () => (
    <Edit>
        <SimpleForm>
            <ReferenceInput source="engineId" reference="engines"/>
            <NumberInput disabled source="id"/>
            <TextInput source="characterName"/>
            <NumberInput source="itemTypeId"/>
            <NumberInput source="upgradeLevel"/>
            <NumberInput source="cost"/>
            <NumberInput source="speed"/>
            <NumberInput source="jump"/>
        </SimpleForm>
    </Edit>
);


export const EngineCreate = () => {
    const {data: typesData} = useGetList('engine_types');
    const [selectedType, setSelectedType] = useState({});

    if (!typesData) {
        return null;
    }

    const handleTypeChange = (event) => {
        for (const type of typesData) {
            if (type.id === event.target.value) {
                setSelectedType(type);
                break;
            }
        }
    };

    return (
        <Create>
            <SimpleForm>
                <AddPropInput type={selectedType}/>
                <TextInput source="characterName"/>
                <NumberInput source="upgradeLevel" defaultValue={0}/>
                <SelectInput
                    source="engineTypeId"
                    choices={typesData}
                    optionText="nameRu"
                    optionValue="id"
                    label="type"
                    onChange={handleTypeChange}
                />
            </SimpleForm>
        </Create>
    );
};

const AddPropInput = (props) => {
    const {setValue} = useFormContext();

    useEffect(() => {
        setValue('dscRu', props.type.dscRu);
        setValue('nameRu', props.type.nameRu);
        setValue('cost', props.type.cost);
        setValue('speed', props.type.speed);
        setValue('jump', props.type.jump);
    }, [setValue, props]);

    return (
        <div>
            <TextInput source="dscRu" fullWidth/>
            <TextInput source="nameRu" fullWidth/>
            <NumberInput source="cost"/>
            <NumberInput source="speed"/>
            <NumberInput source="jump"/>
        </div>
    );
};
