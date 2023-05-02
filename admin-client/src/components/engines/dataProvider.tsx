import {
    DataProvider,
    DeleteManyResult,
    fetchUtils,
    GetManyReferenceResult,
    GetManyResult,
    RaRecord,
    UpdateManyResult
} from "react-admin";

const apiUrl = '/api';
const httpClient = fetchUtils.fetchJson;

export const dataProvider: DataProvider = {
    getList: (resource) => {
        const url = `${apiUrl}/${resource}`;

        return httpClient(url).then(({json}) => ({
            data: json,
            total: json.length,
        }));
    },

    getOne: (resource, params) => httpClient(`${apiUrl}/${resource}/${params.id}`).then(({json}) => ({
        data: {
            ...json,
            characterName: json.item.characterName,
            slotId: json.item.slotId,
            itemTypeId: json.item.itemTypeId,
            upgradeLevel: json.item.upgradeLevel,
            cost: json.item.cost
        },
    })),

    update: (resource, params) => httpClient(`${apiUrl}/${resource}/${params.id}`, {
        method: 'PUT',
        body: JSON.stringify(params.data),
    }).then(({json}) => ({data: json})),

    create: (resource, params) => httpClient(`${apiUrl}/${resource}`, {
        method: 'POST',
        body: JSON.stringify(params.data),
    }).then(({json}) => ({
        data: {...params.data, id: json.id},
    })),

    delete: (resource, params) => httpClient(`${apiUrl}/${resource}/${params.id}`, {
        method: 'DELETE',
    }).then(({json}) => ({data: json})),

    updateMany: function <RecordType extends RaRecord = any>(): Promise<UpdateManyResult<RecordType>> {
        throw new Error("Function not implemented.");
    },
    getMany: function <RecordType extends RaRecord = any>(): Promise<GetManyResult<RecordType>> {
        throw new Error("Function not implemented.");
    },
    getManyReference: function <RecordType extends RaRecord = any>(): Promise<GetManyReferenceResult<RecordType>> {
        throw new Error("Function not implemented.");
    },
    deleteMany: function <RecordType extends RaRecord = any>(): Promise<DeleteManyResult<RecordType>> {
        throw new Error("Function not implemented.");
    }
};