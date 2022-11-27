const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            data: [],
            clients: [],
            client: {},
            name: '',
            surname: '',
            email: '',
            updatedClient: undefined,
            actualClient: {},
            modifyName: '',
            modifySurname: '',
            modifyEmail: '',
            modifyAttribute: '',
            attribute: '',
            fnModify: '',
        }
    },
    created() {
        this.getData();
    },
    mounted() {

    },
    methods: {
        getData() {
            axios.get("http://localhost:8080/clients")
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    this.clients = data._embedded.clients
                    console.log(data);
                    console.log(this.clients);
                })
                .catch(error => console.error(error))
        },
        createClient() {
            if (this.email.toLowerCase().includes(".com") && this.email.includes("@")) {
                this.client = {
                    name: this.name,
                    surname: this.surname,
                    email: this.email,
                }
                this.postClient(this.client)
            }
        },
        postClient(client) {
            axios.post("http://localhost:8080/clients", client)
                .then(res => this.getData())
                .catch(error => console.error(error))
        },
        deleteClient(client) {
            axios.delete(client._links.client.href)
                .then(res => this.getData())
                .catch(error => console.error(error))
        },
        clientActual(client) {
            this.actualClient = client;
            this.modifyName = this.actualClient.name;
            this.modifySurname = this.actualClient.surname;
            this.modifyEmail = this.actualClient.email;
        },
        reloadForm() {
            this.modifyName = ''
            this.modifySurname = ''
            this.modifyEmail = ''
            this.modifyAttribute = ''
        },
        clientModify() {
            if (this.modifyEmail.toLowerCase().includes(".com") && this.modifyEmail.includes("@")) {
                this.actualClient.name = this.modifyName;
                this.actualClient.surname = this.modifySurname;
                this.actualClient.email = this.modifyEmail;

                this.updateClient(this.actualClient);
            }

            this.modifyName = '';
            this.modifySurname = '';
            this.modifyEmail = '';
        },
        updateClient(client) {
            axios.put(client._links.client.href, client)
                .then(res => this.getData())
                .catch(error => console.error(error))
        },
        attributeName(client) {
            this.attribute = 'Name'
            this.actualClient = client;
            this.modifyAttribute = client.name;
        },
        attributeSurname(client) {
            this.attribute = 'Surname'
            this.actualClient = client;
            this.modifyAttribute = client.surname;
        },
        attributeEmail(client) {
            this.attribute = 'Email'
            this.actualClient = client;
            this.modifyAttribute = client.email;
        },
        nameModify(client) {
            if (this.attribute === 'Name') {
                axios.patch(client._links.client.href, {name: this.modifyAttribute})
                    .then(res => {
                        this.attribute = '';
                        this.modifyAttribute = '';
                        this.getData()})
                    .catch(error => console.error(error))
            } else if (this.attribute === 'Surname') {
                axios.patch(client._links.client.href, {surname: this.modifyAttribute})
                    .then(res => {
                        this.attribute = '';
                        this.modifyAttribute = '';
                        this.getData()})
                    .catch(error => console.error(error))
            } else if (this.attribute === 'Email') {
                if (this.modifyAttribute.toLowerCase().includes(".com") && this.modifyAttribute.includes("@")) {
                    axios.patch(client._links.client.href, {email: this.modifyAttribute})
                        .then(res => {
                            this.attribute = '';
                            this.modifyAttribute = '';
                            this.getData()})
                        .catch(error => console.error(error))
                }
            }
        },

    },
    computed: {

    }
})
app.mount('#app')