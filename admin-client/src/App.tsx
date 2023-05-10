import {Admin, Resource} from "react-admin";
import {EngineCreate, EngineEdit, EngineList} from "./components/engines/engines";
import {dataProvider} from "./components/dataProvider";
import {CargoHookCreate, CargoHookEdit, CargoHookList} from "./components/cargoHooks/cargoHooks";

const App = () => (
    <Admin dataProvider={dataProvider}>
        <Resource name="engines" list={EngineList} edit={EngineEdit} create={EngineCreate}/>
        <Resource name="cargoHooks" list={CargoHookList} edit={CargoHookEdit} create={CargoHookCreate}/>
    </Admin>
);

export default App;