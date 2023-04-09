import {Admin, Resource} from "react-admin";
import {EngineCreate, EngineEdit, EngineList} from "./components/engines/engines";
import {dataProvider} from "./components/engines/dataProvider";

const App = () => (
    <Admin dataProvider={dataProvider}>
        <Resource name="engines" list={EngineList} edit={EngineEdit} create={EngineCreate}/>
    </Admin>
);

export default App;