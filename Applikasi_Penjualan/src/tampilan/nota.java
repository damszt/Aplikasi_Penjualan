/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tampilan;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSpinner;
import koneksi.koneksi;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author zz
 */
public class nota extends javax.swing.JFrame {
public String id, nama, jenis, telp,email, almt;
public String kdbrg, nmbrg, jenisbrg, hb, hj, stok;
private Connection conn = new koneksi().connect();
private DefaultTableModel tabmode;
    /**
     * Creates new form nota
     */
    public nota() {
        initComponents();
        String kd = UserID.getUserLogin();
        idksr.setText(kd);
        nama();
        kosong();
        aktif();
        autonumber();
    }
    
    
    /*private void cetak(){
        Map params = new HashMap();
        //buat data dummy
        List<DummyData> listOfDummyData = new ArrayList<DummyData>();
        DummyData dummyData = new DummyData();
        for (int i = 0; i < 28; ++i) {
            dummyData = new DummyData();
            dummyData.setFirst("Meihta");
            dummyData.setMiddle("Dwiguna");
            dummyData.setLast("Saputra");
            listOfDummyData.add(dummyData);
        }
 
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listOfDummyData);
        System.out.println(dataSource.getRecordCount());
        params.put("author", "by : mdsaputra.wordpress.com");
        params.put("dataSource", dataSource);
        //net.sf.jasperreports.engine.JRDataSource
        try {
            JasperPrint printer = JasperFillManager.fillReport(getClass().getResourceAsStream("TutorialJasper.jasper"), params, new JREmptyDataSource());
            //generate kedalam file report.pdf
            JasperExportManager.exportReportToPdfFile(printer, "C:/report.pdf");
            //tampilkan print viewer/dialog
            //ya benar, jasper memiliki print dialog sendiri, kerenkan?
            //jadi tidak usah repot membuka file pdf yang sudah digenerate, dan memanggil fungsi print bawaan java
            JasperViewer jv = new JasperViewer(printer);
            jv.show();
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }*/
    public void cetak(){
        try {
            String path ="./src/report/reportnota.jasper";
            HashMap parameter = new HashMap();
            parameter.put("id_nota", txtidnota.getText());
            JasperPrint print = JasperFillManager.fillReport(path, parameter, conn);
            //JasperExportManager.exportReportToPdfFile(path, "./src/report/reportnota.jasper.pdf");
            JasperViewer.viewReport(print, false);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane,"Dokumen Tidak Ada"+ ex);
        }
    }
    
   /* public void toPDF(){
    try{
    JasperReport jasperReport = null;
    InputStream path=this.getClass().getResourceAsStream("reportnota.jrxml");
    JasperPrint jasperPrint = null;
    jasperReport = JasperCompileManager.compileReport(path);
    HashMap parameters = new HashMap();
    jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,conn);
    JaspervViewer.viewReport(jasperPrint,false);
    } catch (JRException ex){
    Logger.getLogger(nota.class.getName()).log.(Level.SEVERE, null, ex);
    }
    }*/
    
    
    
  

    
    protected void nama(){
        try{
            String sql ="SELECT * FROM kasir WHERE id_kasir='"+idksr.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            if (hasil.next()){
                namaksr.setText(hasil.getString("nama_kasir"));
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "data gagal dipanggil"+e);
        }
    }
    
    protected void aktif(){
        txtqty.requestFocus();
        jtgl.setEditor(new JSpinner.DateEditor(jtgl,"yyyy/MM/dd HH:mm:ss"));
        Object[]Baris = {"KD Barang","Nama","Harga Beli","Harga Jual","QTY","Total"};
        tabmode = new DefaultTableModel(null, Baris);
        tblnota.setModel(tabmode);
    }
    
    protected void kosong(){
        txtidp.setText("");
        txtnm.setText("");
        txtmailp.setText("");
        txtalmt.setText("");
        txtkdbrg.setText("");
        txtnmbrg.setText("");
        txthb.setText("");
        txthj.setText("");
        txtstok.setText("");
        txtqty.setText("");
        txttotal.setText("");
        txtttotal.setText("");
        txtbayar.setText("");
        txtkembalian.setText("");
    }
    
    protected void autonumber(){
    try {
        String sql = "SELECT id_nota From nota order by id_nota asc";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
       txtidnota.setText("IN0001");
       while (rs.next()){
           String id_nota = rs.getString("id_nota").substring(2);
           int AN = Integer.parseInt(id_nota)+1;
           String Nol = "";
           if (AN<10)
           {Nol = "000";}
           else if(AN<100)
             {Nol = "00";}
             else if(AN<1000)
             {Nol = "0";} 
             else if(AN<1000)
             {Nol = "";}  
           txtidnota.setText("IN"+Nol+ AN);    
  }
    } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "AUTO NUMBER GAGAL!!"+e);
        }
    }
    
    public void updatestok(){
        DefaultTableModel table2 =(DefaultTableModel) tblnota.getModel();
        for (int i=0; i < table2.getRowCount(); i++){
            String kdbrg = table2.getValueAt(i, 0).toString();
            String qtybarang = table2.getValueAt (i, 4).toString();
            try{
                String sql = "SELECT * FROM barang where kdbrg = '"+kdbrg+"'";
                Statement stat =  conn.createStatement();
                ResultSet hasil = stat.executeQuery(sql);
                if (hasil.next()){
                   String kodebarang = hasil.getString("kdbrg");
                   String stokygingindikurangi = hasil.getString("stok");
                   int sisastock = Integer.valueOf(stokygingindikurangi)- Integer.valueOf(qtybarang);
                   String query ="update barang set stok = '"+sisastock+"' where kdbrg = '"+kodebarang+"'";
                   //System.out.println(query);
                   stat.executeUpdate(query);
                
            }
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, " Stok telah diubah");
    }
        
 }}
    
    public void itemTerpilih(){
        popuppelanggan Pp = new popuppelanggan();
        Pp.plgn = this;
        txtidp.setText(id);
        txtnm.setText(nama);
        txtmailp.setText(email);
        txtalmt.setText(almt);
    }
    
    public void itemTerpilihBrg(){
        popupbarang Pbrg = new popupbarang();
        Pbrg.brg = this;
        txtkdbrg.setText(kdbrg);
        txtnmbrg.setText(nmbrg);
        txthb.setText(hb);
        txthj.setText(hj);
        txtstok.setText(stok);
        txtqty.requestFocus();
    }
    
    public void hitung(){
        int total = 0;
        for (int i =0; i<tblnota.getRowCount(); i++){
                int amount = Integer.valueOf(tblnota.getValueAt(i, 5).toString());
                total += amount;
        }
        txtttotal.setText(Integer.toString(total));
    }
    
    
      public void kembali(){
          int kembali =
                Integer.parseInt(txtbayar.getText())-Integer.parseInt(txtttotal.getText());
                txtkembalian.setText("Rp."+kembali);
      }
      
            
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        namaksr = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtidnota = new javax.swing.JTextField();
        bcarip = new javax.swing.JButton();
        txtidp = new javax.swing.JTextField();
        txtnm = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtalmt = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jtgl = new javax.swing.JSpinner();
        txtnmbrg = new javax.swing.JTextField();
        txthb = new javax.swing.JTextField();
        txtqty = new javax.swing.JTextField();
        txthj = new javax.swing.JTextField();
        txttotal = new javax.swing.JTextField();
        btambah = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblnota = new javax.swing.JTable();
        bsimpan = new javax.swing.JButton();
        bbatal = new javax.swing.JButton();
        bkeluar = new javax.swing.JButton();
        bhapus = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtttotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtkdbrg = new javax.swing.JTextField();
        bcaribrg = new javax.swing.JButton();
        idksr = new javax.swing.JLabel();
        txtbayar = new javax.swing.JTextField();
        txtkembalian = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txtmailp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        bsendmail = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtstok = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("ID Nota");

        jLabel3.setText("Data Pelanggan");

        jLabel4.setText("ID Pelanggan");

        jLabel5.setText("Nama");

        jLabel6.setText("Alamat");

        namaksr.setText("Nama Kasir");

        jLabel8.setText("Tgl Nota");

        jLabel9.setText("Data Barang");

        jLabel10.setText("Nama Barang");

        jLabel11.setText("Harga Beli");

        jLabel12.setText("Harga Jual");

        txtidnota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidnotaActionPerformed(evt);
            }
        });

        bcarip.setText("Cari");
        bcarip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcaripActionPerformed(evt);
            }
        });

        txtidp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidpActionPerformed(evt);
            }
        });

        txtnm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmActionPerformed(evt);
            }
        });

        txtalmt.setColumns(20);
        txtalmt.setRows(5);
        jScrollPane1.setViewportView(txtalmt);

        jLabel13.setText("QTY");

        jLabel14.setText("Total");

        jtgl.setModel(new javax.swing.SpinnerDateModel());
        jtgl.setEditor(new javax.swing.JSpinner.DateEditor(jtgl, "dd-MM-yyyy HH:mm:ss"));

        txtnmbrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmbrgActionPerformed(evt);
            }
        });

        txthb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthbActionPerformed(evt);
            }
        });

        txtqty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtqtyActionPerformed(evt);
            }
        });

        txthj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthjActionPerformed(evt);
            }
        });

        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });

        btambah.setText("Tambah");
        btambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btambahActionPerformed(evt);
            }
        });

        tblnota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblnota);

        bsimpan.setText("Simpan");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
            }
        });

        bbatal.setText("Batal");
        bbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbatalActionPerformed(evt);
            }
        });

        bkeluar.setText("Keluar");
        bkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkeluarActionPerformed(evt);
            }
        });

        bhapus.setText("Hapus");
        bhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusActionPerformed(evt);
            }
        });

        jLabel15.setText("Tota Hargal");

        jLabel1.setText("Transaksi");

        jLabel18.setText("KD Barang");

        txtkdbrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkdbrgActionPerformed(evt);
            }
        });
        txtkdbrg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtkdbrgKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtkdbrgKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtkdbrgKeyTyped(evt);
            }
        });

        bcaribrg.setText("Cari");
        bcaribrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcaribrgActionPerformed(evt);
            }
        });

        idksr.setText("ID Kasir");

        txtbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbayarActionPerformed(evt);
            }
        });

        txtkembalian.setText("jLabel7");

        jButton1.setText("Bayar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtmailp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmailpActionPerformed(evt);
            }
        });

        jLabel7.setText("Email");

        bsendmail.setText("Send @");
        bsendmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsendmailActionPerformed(evt);
            }
        });

        jLabel16.setText("Stok");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(31, 31, 31)
                        .addComponent(txtidp, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(bcarip))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel5)
                        .addGap(80, 80, 80)
                        .addComponent(txtnm, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(622, 622, 622)
                        .addComponent(txtkembalian))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel1))
                                .addGap(62, 62, 62)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(141, 141, 141)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btambah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel16))
                                        .addGap(71, 71, 71)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txthj, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txttotal)
                                            .addComponent(txtstok)
                                            .addComponent(txthb, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtnmbrg, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtkdbrg, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(bcaribrg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(bkeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bsendmail, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(165, 165, 165)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(83, 83, 83)
                                .addComponent(txtmailp, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(bbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(270, 270, 270)
                                .addComponent(jButton1)))
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtttotal, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(txtbayar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(idksr)
                                .addGap(406, 406, 406)
                                .addComponent(namaksr))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(70, 70, 70)
                                .addComponent(txtidnota, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(141, 141, 141)
                                .addComponent(jLabel8)
                                .addGap(95, 95, 95)
                                .addComponent(jtgl, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(namaksr))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(idksr)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtidnota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel8))))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addComponent(jLabel3)
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addComponent(txtidp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addComponent(bcarip, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(9, 9, 9))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel18))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtkdbrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bcaribrg, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel5))
                            .addComponent(txtnm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtmailp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(67, 67, 67)
                                .addComponent(jLabel1))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtnmbrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txthb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txthj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btambah)))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bhapus)
                    .addComponent(bkeluar)
                    .addComponent(bsendmail)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bsimpan)
                            .addComponent(bbatal)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addGap(18, 18, 18)
                .addComponent(txtkembalian)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txthbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthbActionPerformed

    private void txthjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthjActionPerformed

    private void txtnmbrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmbrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmbrgActionPerformed

    private void txtkdbrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkdbrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkdbrgActionPerformed

    private void bcaripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcaripActionPerformed
        // TODO add your handling code here:
        popuppelanggan Pp = new popuppelanggan();
        Pp.plgn = this;
        Pp.setVisible(true);
        Pp.setResizable(false);
    }//GEN-LAST:event_bcaripActionPerformed

    private void bcaribrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcaribrgActionPerformed
        // TODO add your handling code here:
        popupbarang Pbrg = new popupbarang();
        Pbrg.brg = this;
        Pbrg.setVisible(true);
        Pbrg.setResizable(false);
    }//GEN-LAST:event_bcaribrgActionPerformed

    private void txtqtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtqtyActionPerformed
        // TODO add your handling code here:
        int xhrgj = Integer.parseInt(txthj.getText());
        int xqty = Integer.parseInt(txtqty.getText());
        int xjml=xhrgj*xqty;
        txttotal.setText(String.valueOf(xjml));
        
        
    }//GEN-LAST:event_txtqtyActionPerformed

    private void btambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btambahActionPerformed
        // TODO add your handling code here:
        try{
            String kode = txtkdbrg.getText();
            String nama = txtnmbrg.getText();
            int hargap = Integer.parseInt(txthb.getText());
            int hargaj = Integer.parseInt(txthj.getText());
            int qty = Integer.parseInt(txtqty.getText());
            int total = Integer.parseInt(txttotal.getText());
        
            
            
            tabmode.addRow(new Object[]{kode,nama,hargap,hargaj,qty,total});
            tblnota.setModel(tabmode);
        }
        catch (Exception e)
        {
            System.out.println("Error : "+e);
        }
        txtkdbrg.setText("");
        txtnmbrg.setText("");
        txthb.setText("");
        txthj.setText("");
        txtstok.setText("");
        txtqty.setText("");
        txttotal.setText("");
        hitung();
    }//GEN-LAST:event_btambahActionPerformed

    private void bhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusActionPerformed
        // TODO add your handling code here:
        int index = tblnota.getSelectedRow();
        tabmode.removeRow(index);
        tblnota.setModel(tabmode);
        hitung();
    }//GEN-LAST:event_bhapusActionPerformed

    private void bsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimpanActionPerformed
        // TODO add your handling code here:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fd = sdf.format(jtgl.getValue());
        String sql = "insert into nota values (?,?,?,?,?,?,?)";
        String zsql = "insert into isi values (?,?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, txtidnota.getText());
            stat.setString(2, fd);
            stat.setString(3, txtidp.getText());
            stat.setString(4, txtmailp.getText());
            stat.setString(5, idksr.getText());
            stat.setString(6, txtbayar.getText());
            stat.setString(7, txtkembalian.getText());
            
            stat.executeUpdate();
            int t = tblnota.getRowCount();
            for(int i =0; i < t; i++)
            {
                String xkd = tblnota.getValueAt(i, 0).toString();
                String xhb = tblnota.getValueAt(i, 2).toString();
                String xhj = tblnota.getValueAt(i, 3).toString();
                String xqty = tblnota.getValueAt(i, 4).toString();
             
           PreparedStatement stat2 = conn.prepareStatement(zsql);
            stat2.setString(1, txtidnota.getText());
            stat2.setString(2, xkd);
            stat2.setString(3, xhb);
            stat2.setString(4, xhj);
            stat2.setString(5, xqty);
            
            stat2.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, " Berhasil Disimpan");
            cetak();
            updatestok();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal Disimpan" +e);
        }
        kosong();
        aktif();
        autonumber();
    }//GEN-LAST:event_bsimpanActionPerformed

    private void bbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbatalActionPerformed
        // TODO add your handling code here:
        kosong();
        aktif();
        autonumber();
    }//GEN-LAST:event_bbatalActionPerformed

    private void bkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkeluarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_bkeluarActionPerformed

    private void txtidnotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidnotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidnotaActionPerformed

    private void txtnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmActionPerformed

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void txtidpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidpActionPerformed

    private void txtbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbayarActionPerformed
        // TODO add your handling code here:
       // int xtharg = Integer.parseInt(txtttotal.getText());
       // int xuang = Integer.parseInt(txtbayar.getText());
        //int xkmbl=xuang-xtharg;
        //txtkembali.setText(String.valueOf(xkmbl));
    }//GEN-LAST:event_txtbayarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here: 
      kembali();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtmailpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmailpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmailpActionPerformed

    private void bsendmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsendmailActionPerformed
        // TODO add your handling code here:
      //  mail_api mail = new mail_api();
       // mail.sendmail(txtmailp.getText(), txtidnota.getText(), txtttotal.getText());
    }//GEN-LAST:event_bsendmailActionPerformed

    private void txtkdbrgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkdbrgKeyPressed
        // TODO add your handling code here:
        /* if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
        DefaultTableModel table = new DefaultTableModel();
        
        table.addColumn("NO");
        table.addColumn("CODE ITEM");
        table.addColumn("NAME ITEM");
        table.addColumn("PRICE");
        table.addColumn("STOCK");
        
        try
        {
            String sql ="SELECT * FROM barang where kdbrg='"+txtkdbrg.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            

            
            while(hasil.next())
            {
                table.addRow(new Object[]{
                    
                    hasil.getString(1),
                    hasil.getString(2),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                        
                });
            }
            tblnota.setModel(table);
        }
        catch(Exception e){
    
}
        }*/
        
    }//GEN-LAST:event_txtkdbrgKeyPressed

    private void txtkdbrgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkdbrgKeyTyped
        // TODO add your handling code here:
        /* if("".equals(txtkdbrg.getText())){
            listed();
        }*/
    }//GEN-LAST:event_txtkdbrgKeyTyped

    private void txtkdbrgKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkdbrgKeyReleased
        // TODO add your handling code here:
       /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
        DefaultTableModel table = new DefaultTableModel();
        
        table.addColumn("NO");
        table.addColumn("CODE ITEM");
        table.addColumn("NAME ITEM");
        table.addColumn("PRICE");
        table.addColumn("STOCK");
        
        try
        {
            String sql ="SELECT * FROM barang where kdbrg='"+txtkdbrg.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            
            while(hasil.next())
            {
                table.addRow(new Object[]{
                    hasil.getString(1),
                    hasil.getString(2),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                        
                });
            }
            tblnota.setModel(table);
        }
        catch(Exception e){
    
}
        }*/
    }//GEN-LAST:event_txtkdbrgKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(nota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(nota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(nota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(nota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new nota().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bbatal;
    private javax.swing.JButton bcaribrg;
    private javax.swing.JButton bcarip;
    private javax.swing.JButton bhapus;
    private javax.swing.JButton bkeluar;
    private javax.swing.JButton bsendmail;
    private javax.swing.JButton bsimpan;
    private javax.swing.JButton btambah;
    private javax.swing.JLabel idksr;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jtgl;
    private javax.swing.JLabel namaksr;
    private javax.swing.JTable tblnota;
    private javax.swing.JTextArea txtalmt;
    private javax.swing.JTextField txtbayar;
    private javax.swing.JTextField txthb;
    private javax.swing.JTextField txthj;
    private javax.swing.JTextField txtidnota;
    private javax.swing.JTextField txtidp;
    private javax.swing.JTextField txtkdbrg;
    private javax.swing.JLabel txtkembalian;
    private javax.swing.JTextField txtmailp;
    private javax.swing.JTextField txtnm;
    private javax.swing.JTextField txtnmbrg;
    private javax.swing.JTextField txtqty;
    private javax.swing.JTextField txtstok;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txtttotal;
    // End of variables declaration//GEN-END:variables
}
