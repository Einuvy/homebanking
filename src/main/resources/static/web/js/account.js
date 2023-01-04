const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get('id'),
            data: [],
            client: {},
            accounts: [],
            account: {},
            transactions: [],
            accountDestinatary: "Destinatary Account...",
            description:"",
            sendOtherAccountOrigin:"Origin Account...",
            sendOtherAccount:"",
            sendOtherAmount:"",
            sendOtherDescription:"",
            recipientClient:{},
        }
    },
    created() {
        this.getData();
    },
    mounted() {
        const body = document.querySelector("body"),
            sidebar = body.querySelector("nav"),
            toggle = body.querySelector(".toggle"),
            modeSwitch = body.querySelector(".toggle-switch"),
            modeText = body.querySelector(".mode-text"),
            mainContent = body.querySelector(".main-accounts")

        toggle.addEventListener("click", () => {
            sidebar.classList.toggle("close");
        });
        modeSwitch.addEventListener("click", () => {
            body.classList.toggle("dark");
            mainContent.classList.toggle("dark")

            if (body.classList.contains("dark")) {
                modeText.innerText = "Light mode";
            } else {
                modeText.innerText = "Dark mode";
            }
        });
    },
    methods: {
        getData() {
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    let USDollar = new Intl.NumberFormat("en-US",
                        {
                            style: "currency",
                            currency: "USD",
                        });
                    this.client = data;
                    this.accounts = this.client.accounts;
                    this.accounts.map(account => account.balance = USDollar.format(account.balance))
                    this.account = this.accounts.find(account => (account.id.toString()) == this.id)
                    this.transactions = this.account.transactions.map(transaction => transaction).sort((a, b) => b.id - a.id);
                    this.transactions.map(transaction => transaction.amount = USDollar.format(transaction.amount))
                })
                .catch(error => console.error(error))
        },
        logOut() {
            axios.post("/api/logout")
                .then((response) => {
                    console.log("signed out!!");
                    window.location.href = response.request.responseURL
                })
        },
        /* sweetTransaction() {
            Swal.fire({
                didOpen:(event)=>{
                    document.querySelector('.myself').addEventListener("click", () =>{
                        this.myselfTransfer()
                    }),
                    document.querySelector('.other').addEventListener("click", () =>{
                        this.otherTransfer()
                    })
                },
                title: "Welcome!",
                text: "",
                html: `
                <div>
                    <h5>Who do you want to transfer?</h5>
                    <div class="d-flex justify-content-around">
                        <button type="button" class="myself btn btn-success">Myself</button>
                        <button type="button" class="other btn btn-info">Other</button>
                    </div>
                </div>
                `,
                customClass: {
                    popup: "text-white borderRadius-15",
                },
            })
        },
        myselfTransfer(){
            Swal.fire({
                title: "You selected transfers for yourself",
                html:`
                <div id="app">
                <div class="m-4 d-flex flex-column">
                  <div>
                    <h5>Select destination account</h5>
                    <select class="destinatary form-select form-select-lg mb-3" aria-label=".form-select-lg example"
                      v-model="accountDestinatary">
                      <option selected>Destinatary Account...</option>
                      <option :value="account.number" v-for="account in accounts" :disabled="account.number === accountOrigin">{{account.number}}</option>
                    </select>
                  </div>
                  <div>
                    <h5>Select Amount</h5>
                    <div class="d-flex">
                      <span class="input-group-text">$</span>
                      <input type="number" class="form-control form-select-lg mb-3" aria-label=".form-select-lg example"
                        v-model="amount">
                      </input>
                    </div>
                  </div>
                  <div class="mb-3">
                    <h5>Description</h5>
                    <input type="text"
                      class="form-control" aria-describedby="helpId" v-model="description">
                    <small id="helpId" class="form-text text-muted"></small>
                  </div>
                  <button type="button" class="send-btn btn btn-info align-self-center" @click="transfer">Send Transfer</button>
                </div>
                </div>

                `,
                customClass: {
                    popup: "text-white borderRadius-15",
                },
                preConfirm: () => {
                    return [
                        this.type = document.querySelector('.type').value,
                        this.color = document.querySelector('.color').value,
                    ]
                }
            })
        },
        otherTransfer(){
            Swal.fire({
                title: "You selected transfers to others",
                customClass: {
                    popup: "text-white borderRadius-15",
                },
            })
        } */
    },
    computed: {

    }
}).mount('#app')